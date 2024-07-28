package com.test.taxicompany.service.user.impl;

import com.test.taxicompany.exception.DriverStatusException;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverObservable driverObservable;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void updateDriverStatus_invalidDriverId() {
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.empty());
        assertThrows(DriverStatusException.class, ()->driverService.updateDriverStatus(1L, 1));
        verify(this.driverRepository, times(0)).save(any(Driver.class));
    }

    @Test
    void updateDriverStatus_invalidStatus() {
        Driver driver = new Driver();
        driver.setAvailabilityStatus(AvailabilityStatus.ON_CALL);
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(driver));
        assertThrows(DriverStatusException.class, ()->driverService.updateDriverStatus(1L, 4));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }

    @Test
    void updateDriverStatus_sameValueUpdate() {
        Driver driver = new Driver();
        driver.setAvailabilityStatus(AvailabilityStatus.ON_CALL);
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(driver));
        when(driverObservable.getDriver(eq(1L))).thenReturn(driver);
        assertTrue(driverService.updateDriverStatus(1L, AvailabilityStatus.ON_CALL.getStatusCode()));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateDriverStatus_updateToAvailable() {
        Driver driver = new Driver();
        driver.setAvailabilityStatus(AvailabilityStatus.ON_CALL);
        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(driver));
        when(driverObservable.getDriver(eq(1L))).thenReturn(driver);
        assertTrue(driverService.updateDriverStatus(1L, AvailabilityStatus.AVAILABLE.getStatusCode()));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }
}