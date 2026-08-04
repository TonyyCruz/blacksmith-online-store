package com.anthony.blacksmithOnlineStore.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .components(
        new Components()
          .addSecuritySchemes(
            "bearerAuth",
            new SecurityScheme()
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
              .bearerFormat("JWT")
        )
      )
      .info(new Info()
          .title("Blacksmith Online Store API")
          .description("REST API for managing a medieval-themed e-commerce.")
          .version("1.0.0")
          .contact(new Contact()
            .name("Anthony Cruz")
            .email("anthony-cruz@outlook.com"))
          .description("""
                REST API for a medieval-themed e-commerce.

                Authentication:

                1. Call POST /auth/login

                2. Copy the returned JWT

                3. Click 'Authorize'

                4. Paste the token

                5. Execute secured endpoints
            """)
      )
      .externalDocs(
        new ExternalDocumentation()
          .description("Blacksmith Online Store API")
          .url("https://github.com/TonyyCruz/blacksmith-online-store")
      );
  }

}
