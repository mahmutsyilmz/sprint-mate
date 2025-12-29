package com.yilmaz.sprintmate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) Configuration
 * 
 * Swagger UI: http://localhost:8080/swagger-ui.html
 * OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sprint Mate API")
                        .version("1.0.0")
                        .description("""
                                Sprint Mate - AI-Powered Agile Simulation Platform API

                                Bu API, junior yazılımcıları gerçek dünya deneyimine hazırlayan
                                Agile simülasyon platformunun backend servislerini sağlar.

                                ## Modüller
                                - **Auth**: GitHub OAuth ile kimlik doğrulama
                                - **Lobby**: Eşleştirme sistemi
                                - **Board**: Kanban board işlemleri
                                - **AI**: Proje üretimi (Internal)
                                """)
                        .contact(new Contact()
                                .name("Sprint Mate Team")
                                .email("contact@sprintmate.com")
                                .url("https://github.com/mahmutsyilmz/sprint-mate"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://api.sprintmate.com").description("Production")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token ile kimlik doğrulama")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
