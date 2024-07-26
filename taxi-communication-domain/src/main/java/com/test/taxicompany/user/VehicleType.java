package com.test.taxicompany.user;

import java.util.HashMap;
import java.util.Map;

public enum VehicleType {
    FOUR_SEATER("4Seater"),
    FOUR_SEATER_WITH_KIDS("4SeaterWithKids"),
    SIX_SEATER("6Seater");

    private String vehicleName;

    VehicleType(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    private static final Map<String, VehicleType> vehicleMap = new HashMap<>();

    static {
        for (VehicleType vehicle : VehicleType.values()) {
            vehicleMap.put(vehicle.vehicleName, vehicle);
        }
    }

    public static VehicleType of(String vehicleName) {
        return vehicleMap.getOrDefault(vehicleName, FOUR_SEATER);
    }
}
