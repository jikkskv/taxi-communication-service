package com.test.taxicompany.queue;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryMessageQueueServiceTest {

    private InMemoryMessageQueueService messageQueueService;

    private static final String MESSAGE_STR = "messageStr";

    Set<String> msgRcvdSet = Collections.synchronizedSet(new HashSet<>());

    private static final AtomicInteger ai = new AtomicInteger(-1);

    InMemoryMessageQueueServiceTest() {
        messageQueueService = new InMemoryMessageQueueService();
    }

    @Test
    void testSendReceiveMessage() throws InterruptedException {
        ExecutorService sendMessagePool = Executors.newFixedThreadPool(10);
        sendMessagePool.submit(() -> {
            while (ai.get() < 1_00_000) {
                messageQueueService.sendMessage(MessageQueueService.BOOKING_TOPIC, MESSAGE_STR + ai.incrementAndGet());
            }
        });

        ExecutorService rcvMessagePool = Executors.newFixedThreadPool(10);
        rcvMessagePool.submit(() -> {
            while (true) {
                if(messageQueueService.hasMessages(MessageQueueService.BOOKING_TOPIC)) {
                    msgRcvdSet.add(messageQueueService.receiveMessage(MessageQueueService.BOOKING_TOPIC));
                }
            }
        });

        Thread.sleep(1000);
        sendMessagePool.shutdown();
        while (messageQueueService.hasMessages(MessageQueueService.BOOKING_TOPIC)) {
            Thread.sleep(100);
        }
        rcvMessagePool.shutdownNow();
        for (int idx = 0; idx < ai.get(); idx++) {
            assertTrue(msgRcvdSet.contains(MESSAGE_STR + idx));
        }
        assertFalse(messageQueueService.hasMessages(MessageQueueService.BOOKING_TOPIC));
    }
}