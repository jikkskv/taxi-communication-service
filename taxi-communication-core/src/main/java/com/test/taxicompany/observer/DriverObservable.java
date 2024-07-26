package com.test.taxicompany.observer;

import com.test.taxicompany.user.Driver;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class DriverObservable {

    private final ConcurrentMap<Long, DriverObserver> observers = new ConcurrentHashMap<>();

    public void addObserver(DriverObserver driverObserver) {
        observers.put(driverObserver.getDriver().getId(), driverObserver);
    }

    public void removeObserver(DriverObserver driverObserver) {
        observers.remove(driverObserver.getDriver().getId());
    }

    public Collection<DriverObserver> getObservers() {
        return observers.values();
    }

    public void broadCastMessage(String message) {
        observers.values().forEach(e -> e.sendMessage(message));
    }

    public void sendMessageToDestination(long driverId, String message) {
        observers.get(driverId).sendMessage(message);
    }

    public Driver getDriver(Long driverId) {
        return observers.get(driverId).getDriver();
    }
}
