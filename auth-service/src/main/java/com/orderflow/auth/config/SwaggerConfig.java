package com.orderflow.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service REST API")
                        .description("Provides user registration, login, and RS256 JWT token generation.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Event Driven OMS Support")));
    }
}
