package com.test.taxicompany.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Data
@Entity
@Table(name = "driver")
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    private String vehicleNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private DriverLicense driverLicenseInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false)
    private AvailabilityStatus availabilityStatus;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DriverLicense {
        private String licenseNo;
    }

    public void setDriverLicenseInfo(String licenseNo) {
        if (Objects.isNull(driverLicenseInfo)) {
            driverLicenseInfo = new DriverLicense();
        }
        driverLicenseInfo.licenseNo = licenseNo;
    }
}
