package com.test.taxicompany.ridestate;

import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RideContextHelper {

    @Autowired
    private RideAvailableState rideAvailableState;

    @Autowired
    private RideAcceptedState rideAcceptedState;

    @Autowired
    private RideInProgressState rideInProgressState;

    @Autowired
    private RideCompletedState rideCompletedState;

    @Autowired
    private RideCancelledState rideCancelledState;

    public RideContext getRideContext(RideStatus rideStatus, RideOrder rideOrder) {
        return new RideContext(getRideStateFromEnum(rideStatus), rideOrder);
    }

    private RideState getRideStateFromEnum(RideStatus rideStatus) {
        return switch (rideStatus) {
            case AVAILABLE -> rideAvailableState;
            case ACCEPTED -> rideAcceptedState;
            case IN_PROGRESS -> rideInProgressState;
            case COMPLETED -> rideCompletedState;
            case CANCELLED -> rideCancelledState;
            default -> rideAvailableState;
        };
    }
}
