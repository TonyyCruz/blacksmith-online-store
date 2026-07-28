package com.anthony.blacksmithOnlineStore.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Blacksmith Online Store API")
                        .description("REST API for managing a medieval-themed e-commerce.")
                        .version("1.0.0"));
    }

}
