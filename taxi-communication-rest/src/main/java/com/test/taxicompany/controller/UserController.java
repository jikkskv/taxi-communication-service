package com.test.taxicompany.controller;

import com.test.taxicompany.dto.DriverRegisterRequest;
import com.test.taxicompany.exception.DriverStatusException;
import com.test.taxicompany.exception.ErrorCode;
import com.test.taxicompany.exception.UserRegisterException;
import com.test.taxicompany.service.user.DriverService;
import com.test.taxicompany.service.user.UserRegistrationService;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private DriverService driverService;

    static final String UPDATE_DRIVER_STATUS_KEY = "updateDriverStatus";

    static final String USER_REGISTER_STATUS_KEY = "userRegisterStatus";

    @PostMapping("/driver/register")
    public ResponseEntity<Map<String, Object>> registerNewDriver(@RequestBody DriverRegisterRequest driverRegisterRequest) {
        Map<String, Object> userRegistrationStatus = new HashMap<>();
        try {
            Long driverId = userRegistrationService.registerDriver(driverRegisterRequest);
            userRegistrationStatus.put(USER_REGISTER_STATUS_KEY, true);
            userRegistrationStatus.put("driverId", driverId);
        } catch (UserRegisterException ex) {
            log.error("Error occurred in UserController.registerNewDriver for driverRegisterRequest: {}", driverRegisterRequest, ex);
            userRegistrationStatus.put(ErrorCode.ERROR_CODE, ex.getErrorCode());
            userRegistrationStatus.put(ErrorCode.ERROR_MESSAGE, ex.getErrorMessage());
            userRegistrationStatus.put(USER_REGISTER_STATUS_KEY, false);
        }
        return ResponseEntity.ok(userRegistrationStatus);
    }

    @GetMapping("/driver")
    public ResponseEntity<List<Driver>> getAllTaxis() {
        List<Driver> driverList = userRegistrationService.getAllDrivers();
        return new ResponseEntity<>(driverList, HttpStatus.OK);
    }

    @PutMapping("/driver/status")
    public ResponseEntity<Map<String, Object>> setDriverStatus(@RequestParam(value = "driverId", required = true) long driverId, @RequestParam(value = "status", required = true) int status) {
        Map<String, Object> driverUpdateStatus = new HashMap<>();
        try {
            boolean updateStatus = driverService.updateDriverStatus(driverId, status);
            driverUpdateStatus.put(UPDATE_DRIVER_STATUS_KEY, updateStatus);
        } catch (DriverStatusException ex) {
            log.error("Error occurred in UserController.setDriverStatus for driverId: {}, status: {}", driverId, status, ex);
            driverUpdateStatus.put(ErrorCode.ERROR_CODE, ex.getErrorCode());
            driverUpdateStatus.put(ErrorCode.ERROR_MESSAGE, ex.getErrorMessage());
            driverUpdateStatus.put(UPDATE_DRIVER_STATUS_KEY, false);
        }
        return ResponseEntity.ok(driverUpdateStatus);
    }
}
