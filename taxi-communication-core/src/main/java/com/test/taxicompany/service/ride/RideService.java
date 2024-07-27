package com.test.taxicompany.service.ride;

import com.test.taxicompany.dto.DriverRideInfo;
import com.test.taxicompany.dto.RideRequest;

import java.util.List;

public interface RideService {

    boolean requestRide(RideRequest rideRequest);

    boolean acceptRide(long rideId, long driverId);

    boolean startRide(long rideId, long driverId);

    List<DriverRideInfo> getAllBookings();

    boolean completeRide(long rideId, long driverId);

    boolean cancelRide(long rideId);
}
