package com.junior.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * url: http://localhost:8080/swagger-ui/index.html#/
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "Authorization";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        List<Server> servers = new ArrayList<>();



        Server testServer = new Server()
                .description("Test Server")
                .url("http://localhost:8080");

        servers.add(testServer);

        Server operateServer = new Server()
                .description("API Server")
                .url("http://54.180.139.123:8080");

        servers.add(operateServer);

        return new OpenAPI()
                .components(new Components())
                .servers(servers)
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("Tripot API") // API의 제목
                .description("The API for tripot project") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
