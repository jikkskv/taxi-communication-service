package com.test.taxicompany.queue.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.taxicompany.dto.LocationInfo;
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
public class InMemoryLocationMessageConsumer implements MessageConsumer {

    @Autowired
    private InMemoryMessageQueueService messageQueueService;

    @Autowired
    private LocationUpdateService locationUpdateService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Scheduled(fixedRate = 1000)
    public void processQueue() {
        if (messageQueueService.hasMessages(getTopicName())) {
            String messageStr = messageQueueService.receiveMessage(getTopicName());
            if (Objects.nonNull(messageStr)) {
                LocationInfo locationInfo;
                try {
                    locationInfo = mapper.readValue(messageStr, LocationInfo.class);
                    locationUpdateService.updateDriverLocation(locationInfo);
                } catch (JsonProcessingException e) {
                    log.error("Error occurred while deserializing the message to LocationInfo.class, message: {}", messageStr);
                } catch (Exception e) {
                    log.error("Error occurred while updating driver location: {}", messageStr);
                }
            }
        }
    }

    @Override
    public String getTopicName() {
        return MessageQueueService.LOCATION_TOPIC;
    }
}
