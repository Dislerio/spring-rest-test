package com.example.dislerio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RestApplication {
	private static final Logger logger = LogManager.getLogger(RestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

}
