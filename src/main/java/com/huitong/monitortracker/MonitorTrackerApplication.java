package com.huitong.monitortracker;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@MapperScan("com.huitong.monitortracker.dao.mybatis")
public class MonitorTrackerApplication {

	public static void main(String[] args) {
		if(ArrayUtils.isNotEmpty(args)) {
			Arrays.stream(args).forEach(arg ->{
				if(StringUtils.trimToEmpty(arg).startsWith("--log.name=")){
					System.setProperty("log.name", arg.substring(arg.indexOf('=') + 1));
				}
			});
		}
		SpringApplication.run(MonitorTrackerApplication.class, args);
	}
}
