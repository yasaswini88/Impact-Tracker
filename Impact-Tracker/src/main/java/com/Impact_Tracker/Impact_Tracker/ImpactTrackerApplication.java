package com.Impact_Tracker.Impact_Tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootApplication
public class ImpactTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImpactTrackerApplication.class, args);
	}

}
