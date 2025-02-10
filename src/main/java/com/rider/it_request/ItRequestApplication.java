package com.rider.it_request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ItRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItRequestApplication.class, args);
	}

}
