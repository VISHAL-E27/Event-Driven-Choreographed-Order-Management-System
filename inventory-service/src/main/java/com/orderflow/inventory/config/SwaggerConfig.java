package com.orderflow.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
    public OpenAPI inventoryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service REST API")
                        .description("Manages stock inventory and availability checks.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Event Driven OMS Support")));
    }
	
	
}
