package com.test.taxicompany.repo;

import com.test.taxicompany.ride.RideStateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideStateLogRepository extends JpaRepository<RideStateLog, Long> {
}
