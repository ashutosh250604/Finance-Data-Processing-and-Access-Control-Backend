package com.finance.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Finance Backend API")
                                                .version("1.0.0")
                                                .description("Backend API for Finance Dashboard with Role-Based Access Control\n\n"
                                                                +
                                                                "## Authentication\n" +
                                                                "1. Register or login to get a JWT token\n" +
                                                                "2. Click 'Authorize' button and enter: `Bearer <your-token>`\n\n"
                                                                +
                                                                "## Test Accounts\n" +
                                                                "- Admin: `admin` / `admin123`\n" +
                                                                "- Analyst: `analyst` / `analyst123`\n" +
                                                                "- Viewer: `viewer` / `viewer123`\n\n" +
                                                                "## Role Permissions\n" +
                                                                "- **VIEWER**: Read dashboard and records\n" +
                                                                "- **ANALYST**: Read + access analytics\n" +
                                                                "- **ADMIN**: Full access + user management")
                                                .contact(new Contact()
                                                                .name("Developer")
                                                                .email("developer@example.com"))
                                                .license(new License()
                                                                .name("Educational Use")
                                                                .url("https://example.com")))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("Enter JWT token")));
        }
}
