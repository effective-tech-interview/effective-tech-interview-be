package com.sparcs.teamf.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
        Info info = new Info()
                .title("TEAM-F API")
                .description("TEAM-F APIs For Clients.")
                .contact(new Contact().email("azure394@gmail.com"))
                .version(springdocVersion);

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
