package com.capstone.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url("http://localhost:8080"));
        serverList.add(new Server().url("https://visssoft.com/"));
        return new OpenAPI()
                .servers(serverList)
                // info
                .info(new Info().title("Educational Material Sharing System API")
                        .description("Sample OpenAPI 3.0")
                        .contact(new Contact()
                                .email("nguyentienkhoi.it@gmail.com")
                                .name("Educational Material Sharing System")
                                .url("http://localhost:8080"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://localhost:8080"))
                        .version("1.0.0"));
    }
}
