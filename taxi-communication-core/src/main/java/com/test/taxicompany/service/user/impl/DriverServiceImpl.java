package com.test.taxicompany.service.user.impl;

import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.service.user.DriverService;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public boolean updateDriverStatus(long driverId, int status) {
        try {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            AvailabilityStatus statusEnum = AvailabilityStatus.of(status)
                    .orElseThrow(() -> new RuntimeException("AvailabilityStatus not found"));

            if (!driver.getAvailabilityStatus().equals(statusEnum)) {
                driver.setAvailabilityStatus(statusEnum);
                driverRepository.save(driver);
            }
            return true;
        } catch (RuntimeException ex) {
            log.error("Error occurred in accepting rideRequest for driverId: {}, and for status: {}", driverId, status, ex);
        }
        return false;
    }
}
