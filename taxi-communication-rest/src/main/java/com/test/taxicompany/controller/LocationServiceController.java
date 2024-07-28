package com.test.taxicompany.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.taxicompany.dto.LocationInfo;
import com.test.taxicompany.queue.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class LocationServiceController {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MessageQueueService messageQueueService;

    @MessageMapping("/location")
    public void updateLocation(LocationInfo locationInfo) {
        try {
            this.messageQueueService.sendMessage(MessageQueueService.LOCATION_TOPIC, mapper.writeValueAsString(locationInfo));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while updating driver location: {}", locationInfo);
        }
    }
}