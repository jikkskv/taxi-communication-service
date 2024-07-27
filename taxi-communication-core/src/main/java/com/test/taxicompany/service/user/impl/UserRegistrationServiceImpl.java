package com.test.taxicompany.service.user.impl;

import com.test.taxicompany.dto.DriverRegisterRequest;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.observer.DriverObserver;
import com.test.taxicompany.repo.DriverRepository;
import com.test.taxicompany.service.user.UserRegistrationService;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import com.test.taxicompany.user.RegistrationType;
import com.test.taxicompany.user.VehicleType;
import com.test.taxicompany.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverObservable driverObservable;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Long registerDriver(DriverRegisterRequest driverRegisterRequest) {
        try {
            validateDriverRequest(driverRegisterRequest);
            Driver driver = getDriver(driverRegisterRequest);
            Driver savedDriver = driverRepository.saveAndFlush(driver);
            Long driverId = savedDriver.getId();
            driver.setId(driverId);
            if (driverId >= 0) {
                driverObservable.addObserver(new DriverObserver(driver, simpMessagingTemplate));
                return driverId;
            }
        } catch (Exception ex) {
            log.error("Error occurred in registerDriver for driverRegisterRequest: {}", driverRegisterRequest, ex);
        }
        return -1L;
    }

    @Override
    public List<Driver> getAllDrivers() {
        Collection<DriverObserver> driverObservers = driverObservable.getObservers();
        return driverObservers.stream().map(e -> e.getDriver()).collect(Collectors.toList());
    }

    private Driver getDriver(DriverRegisterRequest driverRegisterRequest) {
        Driver driver = new Driver();
        driver.setName(driverRegisterRequest.name());
        driver.setEmailId(driverRegisterRequest.email());
        driver.setPhoneNo(driverRegisterRequest.phone());
        driver.setPassword(driverRegisterRequest.password());   //hash before storing in db
        driver.setRegistrationType(Objects.isNull(driverRegisterRequest.phone()) ? RegistrationType.MOBILE_NO : RegistrationType.EMAIL);
        driver.setDriverLicenseInfo(driverRegisterRequest.licenseNumber());
        driver.setVehicleType(VehicleType.of(driverRegisterRequest.vehicleType()));
        driver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        return driver;
    }

    private void validateDriverRequest(DriverRegisterRequest driverRegisterRequest) {
        Assert.hasText(driverRegisterRequest.name(), () -> "Please provide a valid name");
        Assert.isTrue(validateEmail(driverRegisterRequest), () -> "Please provide a valid Email Id");
        Assert.hasText(driverRegisterRequest.phone(), () -> "Please provide a valid phone no");
        Assert.hasText(driverRegisterRequest.password(), () -> "Please provide a valid password");
        Assert.hasText(driverRegisterRequest.vehicleNumber(), () -> "Please provide a valid vehicleNumber");
        Assert.hasText(driverRegisterRequest.licenseNumber(), () -> "Please provide a valid licenseNumber");
    }

    private boolean validateEmail(DriverRegisterRequest driverRegisterRequest) {
        return ValidationUtil.isEmail(driverRegisterRequest.email());
    }
}
