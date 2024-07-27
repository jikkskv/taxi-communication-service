package com.test.taxicompany.service.location.impl;

import com.test.taxicompany.dto.LocationInfo;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.service.location.LocationUpdateService;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LocationUpdateServiceImplTest {

    @Mock
    private DriverObservable driverObservable;

    @InjectMocks
    private LocationUpdateServiceImpl locationUpdateService;

    @Test
    void updateDriverLocation_withValidCoOrdinate() {
        LocationInfo locationInfo = new LocationInfo(1L, 101.1, 103.2);
        Driver driver = new Driver();
        when(driverObservable.getDriver(eq(1L))).thenReturn(driver);
        locationUpdateService.updateDriverLocation(locationInfo);
        assertNotNull(driver.getCoOrdinate());
        assertEquals(driver.getCoOrdinate().getLatitude(), 103.2);
        assertEquals(driver.getCoOrdinate().getLongitude(), 101.1);
    }

    @Test
    void updateDriverLocation_withNullLocationInfo() {
        locationUpdateService.updateDriverLocation(null);
    }
}