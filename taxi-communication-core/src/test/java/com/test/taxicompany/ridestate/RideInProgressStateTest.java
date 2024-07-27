package com.test.taxicompany.ridestate;

import com.test.taxicompany.repo.RideStateLogRepository;
import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStateLog;
import com.test.taxicompany.ride.RideStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RideInProgressStateTest {

    @Mock
    private RideStateLogRepository rideStateLogRepository;

    @InjectMocks
    private RideInProgressState rideInProgressState;

    @Test
    void handleState() {
        RideOrder rideOrder = new RideOrder();
        RideContext rideContext = new RideContext(rideInProgressState, rideOrder);
        rideInProgressState.handleState(rideContext);
        verify(rideStateLogRepository, times(1)).save(any(RideStateLog.class));
    }
}