package br.com.gs1.gs1.infra.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI sensoriumOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GS1 - API")
                        .description("API documentation for FIAP ADS 2025 'Global Solution 1' project")
                        .version("v1.0"));
    }
}

