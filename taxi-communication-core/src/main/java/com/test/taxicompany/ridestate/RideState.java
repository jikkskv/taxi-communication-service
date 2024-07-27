package com.test.taxicompany.ridestate;

import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStateLog;
import com.test.taxicompany.ride.RideStatus;

public interface RideState {

    void handleState(RideContext rideContext);

    RideStatus getState();

    default RideStateLog getRideStateLog(RideOrder rideOrder) {
        RideStateLog rideStateLog = new RideStateLog();
        rideStateLog.setRideOrder(rideOrder);
        rideStateLog.setRideStatus(getState());
        return rideStateLog;
    }
}
