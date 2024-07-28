package com.test.taxicompany.ridestate;

import com.test.taxicompany.repo.RideOrderRepository;
import com.test.taxicompany.repo.RideStateLogRepository;
import com.test.taxicompany.ride.RideOrder;
import com.test.taxicompany.ride.RideStateLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RideCompletedStateTest {

    @Mock
    private RideOrderRepository rideOrderRepository;

    @Mock
    private RideStateLogRepository rideStateLogRepository;

    @InjectMocks
    private RideCompletedState rideCompletedState;

    @Test
    void handleState() {
        RideOrder rideOrder = new RideOrder();
        RideOrder savedRiderOrder = new RideOrder();
        RideContext rideContext = new RideContext(rideCompletedState, rideOrder);
        when(this.rideOrderRepository.save(rideOrder)).thenReturn(savedRiderOrder);
        rideCompletedState.handleState(rideContext);
        verify(rideStateLogRepository, times(1)).save(any(RideStateLog.class));
        verify(rideStateLogRepository, times(1)).save(any(RideStateLog.class));
    }
}