package com.test.taxicompany.observer;

import com.test.taxicompany.user.Driver;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class DriverObserver implements IDriverObserver {

    private Driver driver;

    private SimpMessagingTemplate simpMessagingTemplate;

    private final String driverName;

    public DriverObserver(Driver driver, SimpMessagingTemplate simpMessagingTemplate) {
        this.driver = driver;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.driverName = driver.getId().toString();
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    public void sendMessage(String message) {
        this.simpMessagingTemplate.convertAndSendToUser(driverName, "/private/driver/message", message);
    }
}
