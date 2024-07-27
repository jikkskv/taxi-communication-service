package com.test.taxicompany.ridestate;

import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RideContext {
    private RideState rideState;

    private RideOrder rideOrder;

    public RideContext(RideState rideState, RideOrder rideOrder) {
        this.rideOrder = rideOrder;
        this.rideState = rideState;
    }

    public void setState(RideState rideState) {
        this.rideState = rideState;
    }

    public RideStatus getState() {
        return this.rideState.getState();
    }

    public void handleNewState() {
        this.rideState.handleState(this);
    }
}
