package com.test.taxicompany.repo;

import com.test.taxicompany.ride.RideOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideOrderRepository extends JpaRepository<RideOrder, Long> {
}
