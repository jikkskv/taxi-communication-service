package com.test.taxicompany.queue.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.taxicompany.dto.LocationInfo;
import com.test.taxicompany.queue.InMemoryMessageQueueService;
import com.test.taxicompany.service.location.LocationUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class InMemoryLocationMessageConsumerTest {

    @Mock
    private InMemoryMessageQueueService messageQueueService;

    @Mock
    private LocationUpdateService locationUpdateService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private InMemoryLocationMessageConsumer inMemoryLocationMessageConsumer;

    @Test
    void processQueue_updateDriverLocation() throws JsonProcessingException {
        LocationInfo locationInfo = new LocationInfo(1L, 101.1, 102.1);
        when(messageQueueService.hasMessages(anyString())).thenReturn(true);
        when(messageQueueService.receiveMessage(anyString())).thenReturn(mapper.writeValueAsString(locationInfo));
        inMemoryLocationMessageConsumer.processQueue();
        verify(locationUpdateService, times(1)).updateDriverLocation(eq(locationInfo));
    }

    @Test
    void processQueue_jsonErrorError() throws JsonProcessingException {
        when(messageQueueService.hasMessages(anyString())).thenReturn(true);
        when(messageQueueService.receiveMessage(anyString())).thenReturn(mapper.writeValueAsString("invalidJsonStr"));
        inMemoryLocationMessageConsumer.processQueue();
        verify(locationUpdateService, times(0)).updateDriverLocation(any());
    }
}