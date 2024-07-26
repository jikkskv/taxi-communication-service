package com.test.taxicompany.queue;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryMessageQueueService implements MessageQueueService {

    private final ConcurrentMap<String, Queue<String>> queue = new ConcurrentHashMap<>();

    InMemoryMessageQueueService() {
        queue.put(LOCATION_TOPIC, new ConcurrentLinkedQueue<>());
        queue.put(BOOKING_TOPIC, new ConcurrentLinkedQueue<>());
    }

    @Override
    public void sendMessage(String topicName, String messageStr) {
        queue.get(topicName).offer(messageStr);
    }

    @Override
    public String receiveMessage(String topicName) {
        return queue.get(topicName).poll();
    }

    @Override
    public boolean hasMessages(String topicName) {
        return !queue.get(topicName).isEmpty();
    }
}
