package com.test.taxicompany.service.ride;

import com.test.taxicompany.dto.DriverRideInfo;
import com.test.taxicompany.dto.RideRequest;

import java.util.List;
import java.util.Map;

public interface RideService {

    boolean requestRide(RideRequest rideRequest);

    boolean acceptRide(long rideId, long driverId);

    List<DriverRideInfo> getAllBookings();
}
