package com.test.taxicompany.ridestate;

import com.test.taxicompany.repo.RideOrderRepository;
import com.test.taxicompany.repo.RideStateLogRepository;
import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStateLog;
import com.test.taxicompany.ride.RideStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RideAcceptedState implements RideState {

    @Autowired
    private RideOrderRepository rideOrderRepository;

    @Autowired
    private RideStateLogRepository rideStateLogRepository;

    @Override
    public void handleState(RideContext rideContext) {
        RideOrder rideOrder = rideContext.getRideOrder();
        rideOrder.setRideStatus(RideStatus.BOOKED);
        RideOrder savedRideOrder = this.rideOrderRepository.save(rideOrder);
        rideContext.setRideOrder(savedRideOrder);
        addRideStateLog(rideContext);
        // As ride is accepted, lock money from credit/debit card
    }

    private void addRideStateLog(RideContext rideContext) {
        RideStateLog rideStateLog = null;
        try {
            rideStateLog = getRideStateLog(rideContext.getRideOrder());
            this.rideStateLogRepository.save(rideStateLog);
        } catch (Exception ex) {
            log.error("Error occurred while saving ride state log: {}", rideStateLog, ex);
        }
    }

    @Override
    public RideStatus getState() {
        return RideStatus.BOOKED;
    }
}
