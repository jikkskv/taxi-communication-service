package com.test.taxicompany.service.user;

import com.test.taxicompany.dto.DriverRegisterRequest;
import com.test.taxicompany.user.Driver;

import java.util.List;

public interface UserRegistrationService {
    Long registerDriver(DriverRegisterRequest driverRegisterRequest);

    List<Driver> getAllDrivers();
}
