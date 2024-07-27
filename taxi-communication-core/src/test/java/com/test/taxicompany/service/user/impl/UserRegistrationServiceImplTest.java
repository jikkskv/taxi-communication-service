package com.test.taxicompany.service.user.impl;

import com.test.taxicompany.dto.DriverRegisterRequest;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.observer.DriverObserver;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverObservable driverObservable;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    void registerDriver_invalidEmailId() {
        DriverRegisterRequest registerRequest = new DriverRegisterRequest("name", "email", "password", "12345678", "licenseNumber",
                "vehicleNumber", "vehicleType", 1D);
        assertEquals(-1L, userRegistrationService.registerDriver(registerRequest));
        verify(this.driverRepository, times(0)).saveAndFlush(any(Driver.class));
        verify(driverObservable, times(0)).addObserver(any(DriverObserver.class));
    }

    @Test
    void registerDriver_validDriverRegisterRequest() {
        DriverRegisterRequest registerRequest = new DriverRegisterRequest("name", "email@xyz.com", "password", "12345678", "licenseNumber",
                "vehicleNumber", "vehicleType", 1D);
        Driver driver = new Driver();
        driver.setId(123456L);
        when(this.driverRepository.saveAndFlush(any(Driver.class))).thenReturn(driver);
        assertEquals(123456L, userRegistrationService.registerDriver(registerRequest));
        verify(driverObservable, times(1)).addObserver(any(DriverObserver.class));
    }

    @Test
    void registerDriver_saveFailed() {
        DriverRegisterRequest registerRequest = new DriverRegisterRequest("name", "email@xyz.com", "password", "12345678", "licenseNumber",
                "vehicleNumber", "vehicleType", 1D);
        Driver driver = new Driver();
        driver.setId(-1L);
        when(this.driverRepository.saveAndFlush(any(Driver.class))).thenReturn(driver);
        assertEquals(-1L, userRegistrationService.registerDriver(registerRequest));
        verify(driverObservable, times(0)).addObserver(any(DriverObserver.class));
    }

    @Test
    void getAllDrivers() {
        Driver driver = new Driver();
        driver.setId(123456L);
        when(driverObservable.getObservers()).thenReturn(Collections.singletonList(new DriverObserver(driver, simpMessagingTemplate)));
        List<Driver> driverList = userRegistrationService.getAllDrivers();
        assertNotNull(driverList);
        assertFalse(driverList.isEmpty());
        assertEquals(driverList.size(), 1);
    }
}