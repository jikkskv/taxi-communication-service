package com.test.taxicompany.dto;

import com.test.taxicompany.location.CoOrdinate;

public record DriverRideInfo(Long driverId, Long rideId, String driverName, String vehicleType, String vehicleNumber,
                             CoOrdinate source,
                             CoOrdinate destination, String rideStatus, double price) {
}
