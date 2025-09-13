package com.mechtrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mechtrackOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.mechtrack.com");
        prodServer.setDescription("Production Server");

        Contact contact = new Contact();
        contact.setEmail("admin@mechtrack.com");
        contact.setName("MechTrack Support");
        contact.setUrl("https://www.mechtrack.com");

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("MechTrack API")
                .version("1.0.0")
                .contact(contact)
                .description("A comprehensive REST API for managing automotive repair jobs and parts inventory. " +
                        "This API provides endpoints for creating, reading, updating, and deleting jobs and parts, " +
                        "with advanced search capabilities and detailed reporting features.")
                .termsOfService("https://www.mechtrack.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
