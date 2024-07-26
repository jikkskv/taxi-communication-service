package com.test.taxicompany.user;

import com.test.taxicompany.ride.RideOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_ride_relation", uniqueConstraints = @UniqueConstraint(columnNames = {"driver_id", "ride_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRideRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", nullable = false)
    private RideOrder rideOrder;

    private LocalDateTime assignedAt;
}
