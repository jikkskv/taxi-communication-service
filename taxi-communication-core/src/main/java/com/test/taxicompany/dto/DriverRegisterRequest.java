package com.test.taxicompany.dto;

public record DriverRegisterRequest(String name, String email, String password, String phone, String licenseNumber,
                                    String vehicleNumber, String vehicleType, double experience) {
}
