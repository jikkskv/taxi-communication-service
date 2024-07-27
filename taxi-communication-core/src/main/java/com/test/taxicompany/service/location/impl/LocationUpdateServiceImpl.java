package com.test.taxicompany.service.location.impl;

import com.test.taxicompany.dto.LocationInfo;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.service.location.LocationUpdateService;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class LocationUpdateServiceImpl implements LocationUpdateService {

    @Autowired
    private DriverObservable driverObservable;

    @Override
    public void updateDriverLocation(LocationInfo locationInfo) {
        if(Objects.nonNull(locationInfo)) {
            Driver driver = driverObservable.getDriver(locationInfo.id());
            if (Objects.nonNull(driver)) {
                driver.getCoOrdinate().setLatitude(locationInfo.latitude());
                driver.getCoOrdinate().setLongitude(locationInfo.longitude());
            }
        }
    }
}
