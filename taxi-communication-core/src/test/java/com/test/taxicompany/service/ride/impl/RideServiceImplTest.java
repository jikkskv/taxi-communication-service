package com.test.taxicompany.service.ride.impl;

import com.test.taxicompany.dto.DriverRideInfo;
import com.test.taxicompany.dto.RideRequest;
import com.test.taxicompany.exception.RideAcceptedException;
import com.test.taxicompany.location.CoOrdinate;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.queue.MessageQueueService;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.repo.DriverRideRelationRepository;
import com.test.taxicompany.repo.RideOrderRepository;
import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStatus;
import com.test.taxicompany.ridestate.RideContext;
import com.test.taxicompany.ridestate.RideContextHelper;
import com.test.taxicompany.user.Driver;
import com.test.taxicompany.user.DriverRideRelation;
import com.test.taxicompany.user.VehicleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideOrderRepository rideOrderRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverObservable driverObservable;

    @Mock
    private DriverRideRelationRepository driverRideRelationRepository;

    @Mock
    private MessageQueueService messageQueueService;

    @Mock
    private RideContextHelper rideContextHelper;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void requestRide_validRide() {
        RideRequest rideRequest = new RideRequest(new CoOrdinate(), new CoOrdinate(), LocalDateTime.now());
        when(rideContextHelper.getRideContext(eq(RideStatus.AVAILABLE), any(RideOrder.class))).thenReturn(Mockito.mock(RideContext.class));
        assertTrue(rideService.requestRide(rideRequest));
    }

    @Test
    void requestRide_errorOccurredSavingRideRequest() {
        RideRequest rideRequest = new RideRequest(new CoOrdinate(), new CoOrdinate(), LocalDateTime.now());
        RideContext rideContext = Mockito.mock(RideContext.class);
        when(rideContextHelper.getRideContext(eq(RideStatus.AVAILABLE), any(RideOrder.class))).thenReturn(rideContext);
        doThrow(RuntimeException.class).when(rideContext).handleNewState();
        assertFalse(rideService.requestRide(rideRequest));
        verify(rideContext, times(1)).handleNewState();
        verify(this.messageQueueService, times(0)).sendMessage(anyString(), anyString());
    }

    @Test
    void requestRide_errorOccurredSendingPushMessage() {
        RideRequest rideRequest = new RideRequest(new CoOrdinate(), new CoOrdinate(), LocalDateTime.now());
        RideContext rideContext = Mockito.mock(RideContext.class);
        when(rideContextHelper.getRideContext(eq(RideStatus.AVAILABLE), any(RideOrder.class))).thenReturn(rideContext);
        doThrow(RuntimeException.class).when(this.messageQueueService).sendMessage(anyString(), anyString());
        assertFalse(rideService.requestRide(rideRequest));
        verify(rideContext, times(1)).handleNewState();
        verify(this.messageQueueService, times(1)).sendMessage(anyString(), anyString());
    }

    @Test
    void acceptRide_invalidRideRequestId() {
        when(rideOrderRepository.findById(eq(1L))).thenReturn(Optional.empty());
        assertThrows(Exception.class, ()->rideService.acceptRide(1L, 1L));
        verify(driverRideRelationRepository, times(0)).save(any(DriverRideRelation.class));
    }

    @Test
    void acceptRide_invalidDriverId() {
        when(rideOrderRepository.findById(eq(1L))).thenReturn(Optional.of(new RideOrder()));
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.empty());
        assertThrows(RideAcceptedException.class, ()->rideService.acceptRide(1L, 1L));
        verify(driverRideRelationRepository, times(0)).save(any(DriverRideRelation.class));
    }

    @Test
    void acceptRide_alreadyBookedRideStatus() {
        RideOrder rideOrder = new RideOrder();
        rideOrder.setRideStatus(RideStatus.BOOKED);
        when(rideOrderRepository.findById(eq(1L))).thenReturn(Optional.of(rideOrder));
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(new Driver()));
        assertThrows(RideAcceptedException.class, ()->rideService.acceptRide(1L, 1L));
        verify(driverRideRelationRepository, times(0)).save(any(DriverRideRelation.class));
    }

    @Test
    void acceptRide_validRide() {
        RideOrder rideOrder = new RideOrder();
        rideOrder.setRideStatus(RideStatus.AVAILABLE);
        when(rideOrderRepository.findById(eq(1L))).thenReturn(Optional.of(rideOrder));
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(new Driver()));
        RideContext rideContext = Mockito.mock(RideContext.class);
        when(rideContextHelper.getRideContext(eq(RideStatus.BOOKED), any(RideOrder.class))).thenReturn(rideContext);
        when(driverObservable.getDriver(eq(1L))).thenReturn(new Driver());
        assertTrue(rideService.acceptRide(1L, 1L));
        verify(driverRideRelationRepository, times(1)).save(any(DriverRideRelation.class));
    }

    @Test
    void getAllBookings() {
        DriverRideRelation driverRideRelation = new DriverRideRelation();
        Driver driver = new Driver();
        driver.setVehicleType(VehicleType.FOUR_SEATER);
        RideOrder rideOrder = new RideOrder();
        rideOrder.setRideStatus(RideStatus.AVAILABLE);
        driverRideRelation.setDriver(driver);
        driverRideRelation.setRideOrder(rideOrder);
        when(driverRideRelationRepository.findAll()).thenReturn(Collections.singletonList(driverRideRelation));
        List<DriverRideInfo> driverRideInfoList = rideService.getAllBookings();
        assertNotNull(driverRideInfoList);
        assertFalse(driverRideInfoList.isEmpty());
        assertEquals(1, driverRideInfoList.size());
    }
}