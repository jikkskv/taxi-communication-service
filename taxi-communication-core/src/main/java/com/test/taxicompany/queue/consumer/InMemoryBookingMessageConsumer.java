package com.test.taxicompany.queue.consumer;

import com.test.taxicompany.observer.DriverObservable;
import com.test.taxicompany.queue.InMemoryMessageQueueService;
import com.test.taxicompany.queue.MessageQueueService;
import com.test.taxicompany.user.AvailabilityStatus;
import com.test.taxicompany.user.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Predicate;

@Component
@Slf4j
public class InMemoryBookingMessageConsumer implements MessageConsumer {

    @Autowired
    private InMemoryMessageQueueService messageQueueService;

    @Autowired
    private DriverObservable driverObservable;

    Predicate<Driver> AVAILABILITY_CHECK = driver -> AvailabilityStatus.AVAILABLE.equals(driver.getAvailabilityStatus());

    @Override
    @Scheduled(fixedRate = 1000)
    public void processQueue() {
        if (messageQueueService.hasMessages(getTopicName())) {
            String messageStr = messageQueueService.receiveMessage(getTopicName());
            if (Objects.nonNull(messageStr)) {
                driverObservable.broadCastMessage(messageStr, AVAILABILITY_CHECK);
            }
        }
    }

    @Override
    public String getTopicName() {
        return MessageQueueService.BOOKING_TOPIC;
    }
}
