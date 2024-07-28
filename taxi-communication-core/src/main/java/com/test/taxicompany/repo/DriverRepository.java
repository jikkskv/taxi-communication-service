package com.test.taxicompany.repo;

import com.test.taxicompany.user.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {


    Optional<Driver> findByEmailId(String emailId);
}
