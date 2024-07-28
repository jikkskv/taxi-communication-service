package com.test.taxicompany.observer;

import com.test.taxicompany.user.Driver;

import java.util.function.Predicate;

public interface IDriverObserver {

    void sendMessage(String message);

    void sendMessage(String message, Predicate<Driver> predicate);
}
