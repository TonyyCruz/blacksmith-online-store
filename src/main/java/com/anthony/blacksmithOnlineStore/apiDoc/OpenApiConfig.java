package com.anthony.blacksmithOnlineStore.apiDoc;

import java.util.List;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

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
          .description(
            """
            API REST para um e-commerce com temática medieval.

            Autenticação:

            1. Crie um usuário em: POST /auth/register 

            2. Faça seu login em: POST /auth/login

            3. Copie o token JWT recebido

            4. Clike no botão 'Authorize' e cole seu token

            5. Agora você pode acessar as rotas protegidas

            Ps: para utilizar as funcionalidades de administrador:

            Usuário: admin
            
            Senha:   P4ssw0rd#
            """
      ))
      .externalDocs(
        new ExternalDocumentation()
          .description("Blacksmith Online Store API")
          .url("https://github.com/TonyyCruz/blacksmith-online-store")
      ).tags(
        List.of(
          new Tag().name("Authentications").description("Authentication and authorization"),
          new Tag().name("Users").description("User management"),
          new Tag().name("Orders").description("Order management"),
          new Tag().name("Payments").description("Payment management"),
          new Tag().name("Ratings").description("Rating management"),
          new Tag().name("Items").description("Item management"),
          new Tag().name("Blacksmiths").description("Blacksmith management"),
          new Tag().name("Admins").description("Admin management")
      ));
  }

  @Bean
  public SwaggerIndexTransformer swaggerIndexTransformer(
    SwaggerUiConfigProperties swaggerUiConfig,
    SwaggerUiOAuthProperties swaggerUiOAuthProperties,
    SwaggerWelcomeCommon swaggerWelcomeCommon,
    ObjectMapperProvider objectMapperProvider) {

    return new SwaggerCustomCssInjector(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
  }
}
