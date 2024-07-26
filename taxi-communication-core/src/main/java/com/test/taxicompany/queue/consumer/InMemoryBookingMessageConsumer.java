package com.test.taxicompany.queue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.queue.InMemoryMessageQueueService;
import com.test.taxicompany.queue.MessageQueueService;
import com.test.taxicompany.service.location.LocationUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class InMemoryBookingMessageConsumer implements MessageConsumer {

    @Autowired
    private InMemoryMessageQueueService messageQueueService;

    @Autowired
    private LocationUpdateService locationUpdateService;

    @Autowired
    private DriverObservable driverObservable;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Scheduled(fixedRate = 1000)
    public void processQueue() {
        if (messageQueueService.hasMessages(getTopicName())) {
            String messageStr = messageQueueService.receiveMessage(getTopicName());
            if (Objects.nonNull(messageStr)) {
                driverObservable.broadCastMessage(messageStr);
            }
        }
    }

    @Override
    public String getTopicName() {
        return MessageQueueService.BOOKING_TOPIC;
    }
}
