package com.test.taxicompany.ridestate;

import com.test.taxicompany.repo.RideOrderRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RideAcceptedStateTest {

    @Mock
    private RideOrderRepository rideOrderRepository;

    @Mock
    private RideStateLogRepository rideStateLogRepository;

    @InjectMocks
    private RideAcceptedState rideAcceptedState;

    @Test
    void handleState() {
        RideOrder rideOrder = new RideOrder();
        RideOrder savedRiderOrder = new RideOrder();
        RideContext rideContext = new RideContext(rideAcceptedState, rideOrder);
        when(this.rideOrderRepository.save(rideOrder)).thenReturn(savedRiderOrder);
        rideAcceptedState.handleState(rideContext);
        verify(rideOrderRepository, times(1)).save(eq(rideOrder));
        verify(rideStateLogRepository, times(1)).save(any(RideStateLog.class));
    }

    @Test
    void handleState_exceptionWhileSaving() {
        RideOrder rideOrder = new RideOrder();
        RideContext rideContext = new RideContext(rideAcceptedState, rideOrder);
        when(this.rideOrderRepository.save(rideOrder)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> rideAcceptedState.handleState(rideContext));
        verify(rideOrderRepository, times(1)).save(eq(rideOrder));
        verify(rideStateLogRepository, times(0)).save(any(RideStateLog.class));
    }
}