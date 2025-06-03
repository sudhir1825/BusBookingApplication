package com.example.BusBookingApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync//to enable Asynchronous
public class BusBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusBookingApplication.class, args);
	}

}

