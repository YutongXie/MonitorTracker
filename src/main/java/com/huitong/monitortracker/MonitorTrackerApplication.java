package com.huitong.monitortracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class MonitorTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorTrackerApplication.class, args);
	}

}
