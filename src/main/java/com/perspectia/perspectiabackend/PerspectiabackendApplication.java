package com.perspectia.perspectiabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PerspectiabackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerspectiabackendApplication.class, args);
	}

}
