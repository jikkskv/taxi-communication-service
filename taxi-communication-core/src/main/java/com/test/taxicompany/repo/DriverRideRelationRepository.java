package com.test.taxicompany.repo;

import com.test.taxicompany.user.DriverRideRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRideRelationRepository extends JpaRepository<DriverRideRelation, Long> {
}
