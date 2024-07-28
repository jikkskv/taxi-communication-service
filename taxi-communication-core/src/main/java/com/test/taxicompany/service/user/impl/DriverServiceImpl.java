package com.test.taxicompany.service.user.impl;

import com.test.taxicompany.exception.BizErrorCodeEnum;
import com.test.taxicompany.exception.DriverStatusException;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.observer.DriverObserver;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.repo.DriverRideRelationRepository;
import com.test.taxicompany.ride.RideStatus;
import com.test.taxicompany.service.user.DriverService;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    public static final List<RideStatus> RIDE_PROGRESS_STATUS_LIST = List.of(RideStatus.AVAILABLE, RideStatus.BOOKED, RideStatus.IN_PROGRESS);

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverObservable driverObservable;

    @Autowired
    private DriverRideRelationRepository driverRideRelationRepository;

    @Override
    public boolean updateDriverStatus(long driverId, int status) {
        try {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new DriverStatusException(BizErrorCodeEnum.INVALID_DRIVER));

            AvailabilityStatus statusEnum = AvailabilityStatus.of(status)
                    .orElseThrow(() -> new DriverStatusException(BizErrorCodeEnum.INVALID_DRIVER_STATUS));
            doRideAlreadyExistsValidation(driverId, statusEnum);
            driver.setAvailabilityStatus(statusEnum);
            driverRepository.save(driver);
            driverObservable.getDriver(driverId).setAvailabilityStatus(statusEnum);
            return true;
        } catch (DriverStatusException ex) {
            log.error("DriverStatusException occurred in updateDriverStatus for for driverId: {}, and for status: {}", driverId, status, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("General Error occurred in updateDriverStatus for driverId: {}, and for status: {}", driverId, status, ex);
        }
        return false;
    }

    private void doRideAlreadyExistsValidation(long driverId, AvailabilityStatus statusEnum) {
        if (AvailabilityStatus.OFF_WORK.equals(statusEnum)) {
            long count = driverRideRelationRepository.findByDriverIdAndInRideStatus(driverId, RIDE_PROGRESS_STATUS_LIST);
            if (count > 0) {
                throw new DriverStatusException(BizErrorCodeEnum.RIDE_NOT_COMPLETED);
            }
        }
    }
}
