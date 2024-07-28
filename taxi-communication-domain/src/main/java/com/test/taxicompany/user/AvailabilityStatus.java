package com.test.taxicompany.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum AvailabilityStatus {
    AVAILABLE(0),
    ON_CALL(1),
    BREAK(2),
    OFF_WORK(3);

    private final int statusCode;

    AvailabilityStatus(int statusCode) {
        this.statusCode = statusCode;
    }


    private static final Map<Integer, AvailabilityStatus> vehicleMap = new HashMap<>();

    static {
        for (AvailabilityStatus availabilityStatus : AvailabilityStatus.values()) {
            vehicleMap.put(availabilityStatus.statusCode, availabilityStatus);
        }
    }

    public static Optional<AvailabilityStatus> of(Integer statusCode) {
        return vehicleMap.containsKey(statusCode) ? Optional.of(vehicleMap.get(statusCode)) : Optional.empty();
    }
}
