package com.sparcs.teamf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TeamfApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamfApplication.class, args);
	}

}
