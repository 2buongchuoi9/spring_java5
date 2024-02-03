package daden.shopaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import daden.shopaa.utils.Constans.HEADER;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableWebSecurity
public class OpenApiConfig {

  private SecurityScheme createAuthorizationScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER)
        .name(HEADER.AUTHORIZATION)
        .description("authorization - authorization in header");
  }

  private SecurityScheme createXClientApiKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.HEADER)
        .name(HEADER.X_CLIENT_ID)
        .description("x-client-id - x-client-id in header");
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(new Components()
            .addSecuritySchemes("Authentication", createAuthorizationScheme())
            .addSecuritySchemes("x-client-id", createXClientApiKeyScheme()))
        .info(new Info().title("My REST API")
            .description("Some custom description of API.")
            .version("1.0").contact(new Contact().name("Da Den")
                .email("www.baeldung.com").url("nguyenssk043@gmail.com"))
            .license(new License().name("License of API")
                .url("API license URL")));
  }

}
