package com.test.taxicompany.queue;

public interface MessageQueueService {

    String LOCATION_TOPIC = "location";

    String BOOKING_TOPIC = "booking";

    void sendMessage(String topicName, String messageStr);

    String receiveMessage(String topicName);

    boolean hasMessages(String topicName);
}
