package com.test.taxicompany.repo;

import com.test.taxicompany.ride.RideStatus;
import com.test.taxicompany.user.DriverRideRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRideRelationRepository extends JpaRepository<DriverRideRelation, Long> {

    @Query("SELECT COUNT(drr) FROM DriverRideRelation drr WHERE drr.driver.id = :driverId AND drr.rideOrder.rideStatus in :rideStatus")
    long findByDriverIdAndInRideStatus(@Param("driverId") Long driverId, @Param("rideStatus") List<RideStatus> rideStatus);
}
