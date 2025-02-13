package com.rider.it_request_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ItRequestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItRequestServiceApplication.class, args);
    }
}
