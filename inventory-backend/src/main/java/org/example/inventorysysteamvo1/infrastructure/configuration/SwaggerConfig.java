package org.example.inventorysysteamvo1.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI / Swagger UI documentation.
 *
 * After the app starts, open:
 *   http://localhost:8080/swagger-ui/index.html
 *
 * How to authorise in Swagger UI:
 *   1. Call POST /api/auth/login and copy the "token" from the response.
 *   2. Click the "Authorize" button (🔒) in Swagger UI.
 *   3. Paste the token (WITHOUT the "Bearer " prefix — Swagger adds it).
 *   4. All subsequent calls will include the Authorization header.
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI inventoryOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                // Attach the JWT security requirement to all endpoints
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, jwtSecurityScheme()));
    }

    // ── Private Helpers ───────────────────────────────────────────────────

    private Info apiInfo() {
        return new Info()
                .title("Inventory System API")
                .description(
                        "REST API for the Inventory Management System.\n\n"
                                + "Authenticate via **POST /api/auth/login** to receive a JWT, "
                                + "then click **Authorize** and paste the token.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Inventory System Team"));
    }

    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste the JWT token obtained from POST /api/auth/login.");
    }
}