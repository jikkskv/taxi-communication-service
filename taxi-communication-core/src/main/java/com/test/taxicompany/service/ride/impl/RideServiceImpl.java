package com.test.taxicompany.service.ride.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.taxicompany.dto.DriverRideInfo;
import com.test.taxicompany.dto.RideRequest;
import com.test.taxicompany.exception.BizErrorCodeEnum;
import com.test.taxicompany.exception.RideAcceptedException;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.queue.MessageQueueService;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.repo.DriverRideRelationRepository;
import com.test.taxicompany.repo.RideOrderRepository;
import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStatus;
import com.test.taxicompany.ridestate.RideContext;
import com.test.taxicompany.ridestate.RideContextHelper;
import com.test.taxicompany.service.ride.RideService;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import com.test.taxicompany.user.DriverRideRelation;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RideServiceImpl implements RideService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public RideServiceImpl() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    private MessageQueueService messageQueueService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverObservable driverObservable;

    @Autowired
    private RideOrderRepository rideOrderRepository;

    @Autowired
    private DriverRideRelationRepository driverRideRelationRepository;

    @Autowired
    private RideContextHelper rideContextHelper;

    @Override
    public boolean requestRide(RideRequest rideRequest) {
        try {
            Map<String, RideOrder> bookingMessage = new HashMap<>();
            RideOrder rideOrder = getRideOrder(rideRequest);
            RideContext rideContext = rideContextHelper.getRideContext(RideStatus.AVAILABLE, rideOrder);
            rideContext.handleNewState();
            RideOrder savedRideOrder = rideContext.getRideOrder();
            bookingMessage.put("bookingRequest", savedRideOrder);
            this.messageQueueService.sendMessage(MessageQueueService.BOOKING_TOPIC, mapper.writeValueAsString(bookingMessage));
            return true;
        } catch (JsonProcessingException ex) {
            log.error("Error occurred while json processing(writeValueAsString) the ride request: {}", rideRequest, ex);
        } catch (PersistenceException | DataAccessException ex) {
            log.error("Error occurred while saving ride order for rideRequest: {}", rideRequest, ex);
        } catch (Exception ex) {
            log.error("General error occurred in processing rideRequest: {}", rideRequest, ex);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean acceptRide(long rideId, long driverId) {
        try {
            RideOrder rideOrder = getLockedRideOrder(rideId);

            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RideAcceptedException(BizErrorCodeEnum.INVALID_DRIVER));

            if (!RideStatus.AVAILABLE.equals(rideOrder.getRideStatus())) {
                throw new RideAcceptedException(BizErrorCodeEnum.RIDE_ALREADY_ACCEPTED);
            }
            RideContext rideContext = rideContextHelper.getRideContext(RideStatus.BOOKED, rideOrder);
            rideContext.handleNewState();
            driver.setAvailabilityStatus(AvailabilityStatus.ON_CALL);
            DriverRideRelation relation = getDriverRideRelation(rideOrder, driver);
            driverRideRelationRepository.save(relation);
            driverRepository.save(driver);
            driverObservable.getDriver(driverId).setAvailabilityStatus(AvailabilityStatus.ON_CALL);
            return true;
        } catch (RideAcceptedException ex) {
            log.error("Error occurred in accepting rideRequest for rideId: {}, and for driverId: {}", rideId, driverId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unknown error Error occurred in accepting rideRequest for rideId: {}, and for driverId: {}", rideId, driverId, ex);
            throw new RideAcceptedException(BizErrorCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public boolean startRide(long rideId, long driverId) {
        try {
            RideOrder rideOrder = rideOrderRepository.findById(rideId)
                    .orElseThrow(() -> new RuntimeException("Ride not found"));

            if (!RideStatus.BOOKED.equals(rideOrder.getRideStatus())) {
                throw new RuntimeException("Ride request not yet accepted.");
            }
            RideContext rideContext = rideContextHelper.getRideContext(RideStatus.IN_PROGRESS, rideOrder);
            rideContext.handleNewState();
            return true;
        } catch (RuntimeException ex) {
            log.error("Error occurred in accepting rideRequest for rideId: {}, and for driverId: {}", rideId, driverId, ex);
        }
        return false;
    }

    @Override
    public boolean completeRide(long rideId, long driverId) {
        try {
            RideOrder rideOrder = rideOrderRepository.findById(rideId)
                    .orElseThrow(() -> new RuntimeException("Ride not found"));

            if (!RideStatus.IN_PROGRESS.equals(rideOrder.getRideStatus())) {
                throw new RuntimeException("Ride request not in progress state");
            }
            RideContext rideContext = rideContextHelper.getRideContext(RideStatus.COMPLETED, rideOrder);
            rideContext.handleNewState();
            return true;
        } catch (RuntimeException ex) {
            log.error("Error occurred in accepting rideRequest for rideId: {}, and for driverId: {}", rideId, driverId, ex);
        }
        return false;
    }

    @Override
    public boolean cancelRide(long rideId) {
        try {
            RideOrder rideOrder = rideOrderRepository.findById(rideId)
                    .orElseThrow(() -> new RuntimeException("Ride not found"));

            RideContext rideContext = rideContextHelper.getRideContext(RideStatus.CANCELLED, rideOrder);
            rideContext.handleNewState();
            return true;
        } catch (RuntimeException ex) {
            log.error("Error occurred in accepting rideRequest for rideId: {}", rideId, ex);
        }
        return false;
    }

    @Override
    public List<DriverRideInfo> getAllBookings() {
        List<DriverRideRelation> rideRelations = driverRideRelationRepository.findAll();
        return mapToDriverRideInfo(rideRelations);
    }

    private List<DriverRideInfo> mapToDriverRideInfo(List<DriverRideRelation> rideRelations) {
        return rideRelations.stream().map(e -> {
            Driver driver = e.getDriver();
            RideOrder rideOrder = e.getRideOrder();
            return new DriverRideInfo(
                    driver.getId(),
                    rideOrder.getId(),
                    driver.getName(),
                    driver.getVehicleType().toString(),
                    driver.getVehicleNumber(),
                    rideOrder.getSource(),
                    rideOrder.getDestination(),
                    rideOrder.getRideStatus().toString(),
                    rideOrder.getPrice()
            );
        }).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public RideOrder getLockedRideOrder(long rideId) {
        return rideOrderRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride Request not found"));
    }

    private DriverRideRelation getDriverRideRelation(RideOrder rideOrder, Driver driver) {
        DriverRideRelation driverRideRelation = new DriverRideRelation();
        driverRideRelation.setRideOrder(rideOrder);
        driverRideRelation.setDriver(driver);
        driverRideRelation.setAssignedAt(LocalDateTime.now());
        return driverRideRelation;
    }

    private RideOrder getRideOrder(RideRequest rideRequest) {
        RideOrder order = new RideOrder();
        order.setPickupTime(rideRequest.pickupTime());
        order.setSource(rideRequest.source());
        order.setDestination(rideRequest.destination());
        order.setRideStatus(RideStatus.AVAILABLE);
        return order;
    }
}
