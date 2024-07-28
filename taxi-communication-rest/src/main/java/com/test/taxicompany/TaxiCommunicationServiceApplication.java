package com.test.taxicompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class TaxiCommunicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiCommunicationServiceApplication.class, args);
    }

}
