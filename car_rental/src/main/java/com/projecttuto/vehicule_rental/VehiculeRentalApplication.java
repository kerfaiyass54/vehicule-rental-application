package com.projecttuto.vehicule_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class VehiculeRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiculeRentalApplication.class, args);
	}

}
