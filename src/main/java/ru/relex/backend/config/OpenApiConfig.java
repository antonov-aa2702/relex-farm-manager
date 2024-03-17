package ru.relex.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Класс для конфигурации OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Определяет конфигурацию OpenAPI, добавляя возможность использования JWT в качестве токена
     * и описание приложения.
     *
     * @return конфигурация OpenAPI
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))

                )
                .info(new Info()
                        .title("Farm Manager API")
                        .description("Тестовое задание для компании РЕЛЭКС.")
                        .version("1.0"));
    }
}
