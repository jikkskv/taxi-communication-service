package com.test.taxicompany.controller;

import com.test.taxicompany.dto.DriverRideInfo;
import com.test.taxicompany.dto.RideRequest;
import com.test.taxicompany.exception.ErrorCode;
import com.test.taxicompany.exception.RideAcceptedException;
import com.test.taxicompany.service.ride.RideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ride")
@Slf4j
public class RideServiceController {

    @Autowired
    private RideService rideService;

    private static final String RIDE_REQUEST_STATUS_KEY = "rideRequestStatus";

    private static final String RIDE_ACCEPT_STATUS_KEY = "rideAcceptStatus";

    private static final String RIDE_START_STATUS_KEY = "rideStartStatus";

    private static final String RIDE_COMPLETED_STATUS_KEY = "rideCompletedStatus";

    private static final String RIDE_CANCELLED_STATUS_KEY = "rideCancelledStatus";

    @PostMapping("/booking")
    public ResponseEntity<Map<String, Object>> requestRide(@RequestBody RideRequest rideRequest) {
        boolean rideStatus = rideService.requestRide(rideRequest);
        Map<String, Object> rideRequestStatus = new HashMap<>();
        rideRequestStatus.put(RIDE_REQUEST_STATUS_KEY, rideStatus);
        return ResponseEntity.ok(rideRequestStatus);
    }

    @PutMapping("/accept")
    public ResponseEntity<Map<String, Object>> acceptRide(@RequestParam(value = "rideId", required = true) long rideId, @RequestParam(value = "driverId", required = true) long driverId) {
        boolean acceptStatus = false;
        Map<String, Object> rideAcceptStatus = new HashMap<>();
        try {
            acceptStatus = rideService.acceptRide(rideId, driverId);
        } catch (RideAcceptedException ex) {
            log.error("Error occurred in RideServiceController.acceptRide rideId: {}, and for driverId: {}", rideId, driverId, ex);
            rideAcceptStatus.put(ErrorCode.ERROR_CODE, ex.getErrorCode());
            rideAcceptStatus.put(ErrorCode.ERROR_MESSAGE, ex.getErrorMessage());
        }
        rideAcceptStatus.put(RIDE_ACCEPT_STATUS_KEY, acceptStatus);
        return ResponseEntity.ok(rideAcceptStatus);
    }

    @PutMapping("/start")
    public ResponseEntity<Map<String, Object>> startRide(@RequestParam(value = "rideId", required = true) long rideId, @RequestParam(value = "driverId", required = true) long driverId) {
        boolean acceptStatus = rideService.startRide(rideId, driverId);
        Map<String, Object> rideStartStatus = new HashMap<>();
        rideStartStatus.put(RIDE_START_STATUS_KEY, acceptStatus);
        return ResponseEntity.ok(rideStartStatus);
    }

    @PutMapping("/complete")
    public ResponseEntity<Map<String, Object>> completeRide(@RequestParam(value = "rideId", required = true) long rideId, @RequestParam(value = "driverId", required = true) long driverId) {
        boolean acceptStatus = rideService.completeRide(rideId, driverId);
        Map<String, Object> rideStartStatus = new HashMap<>();
        rideStartStatus.put(RIDE_COMPLETED_STATUS_KEY, acceptStatus);
        return ResponseEntity.ok(rideStartStatus);
    }

    @PutMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelRide(@RequestParam(value = "rideId", required = true) long rideId) {
        boolean acceptStatus = rideService.cancelRide(rideId);
        Map<String, Object> rideStartStatus = new HashMap<>();
        rideStartStatus.put(RIDE_CANCELLED_STATUS_KEY, acceptStatus);
        return ResponseEntity.ok(rideStartStatus);
    }

    @GetMapping("/booking")
    public ResponseEntity<List<DriverRideInfo>> getAllBookings() {
        return ResponseEntity.ok(rideService.getAllBookings());
    }
}
