package com.huitong.monitortracker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@MapperScan("com.huitong.monitortracker.dao.mybatis")
public class MonitorTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorTrackerApplication.class, args);
	}

}
