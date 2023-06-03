package com.sparcs.teamf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TeamfApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name",
            "application,auth,domain,config,rds,redis,email,gpt,nickname");
        SpringApplication.run(TeamfApplication.class, args);
    }

}
