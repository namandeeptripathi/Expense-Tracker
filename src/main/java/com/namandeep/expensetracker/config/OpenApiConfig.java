package com.namandeep.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configures the base metadata rendered by OpenAPI and Swagger UI. */
@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI expenseTrackerOpenApi(ApiProperties apiProperties) {
        return new OpenAPI().info(new Info()
                .title(apiProperties.title())
                .description(apiProperties.description())
                .version(apiProperties.version()));
    }
}
