package com.test.taxicompany.queue.consumer;

import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.queue.InMemoryMessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class InMemoryBookingMessageConsumerTest {

    @Mock
    private InMemoryMessageQueueService messageQueueService;

    @Mock
    private DriverObservable driverObservable;

    @InjectMocks
    private InMemoryBookingMessageConsumer messageConsumer;

    @Test
    void processQueue_checkIfBroadCastExecuted() {
        String messageStr = "messageStr";
        when(messageQueueService.hasMessages(anyString())).thenReturn(true);
        when(messageQueueService.receiveMessage(anyString())).thenReturn(messageStr);
        messageConsumer.processQueue();
        verify(driverObservable, times(1)).broadCastMessage(eq(messageStr));
    }
}