package com.tikjuti.bus_ticket_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BusTicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTicketBookingApplication.class, args);
	}

}
