package com.pragma.ms_reportes.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Reports API",
                version = "1.0",
                description = "API de gestión de reportes"
        )
)
public class OpenApiConfig {
}
