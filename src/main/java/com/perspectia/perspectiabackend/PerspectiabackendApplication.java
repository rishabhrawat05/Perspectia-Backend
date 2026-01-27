package com.perspectia.perspectiabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class PerspectiabackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerspectiabackendApplication.class, args);
	}

}
