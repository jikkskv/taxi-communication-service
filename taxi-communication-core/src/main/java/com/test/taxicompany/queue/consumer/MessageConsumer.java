package com.test.taxicompany.queue.consumer;

public interface MessageConsumer {

    void processQueue();

    String getTopicName();
}
