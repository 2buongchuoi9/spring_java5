package daden.shopaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig {

    // private final long MAX_AGE_SECS = 3600;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")
    // // .allowedOriginPatterns("*")
    // .allowedOrigins("http://localhost:5173")
    // .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    // .allowedHeaders("*")
    // .allowCredentials(true)

    // .maxAge(MAX_AGE_SECS);
    // }

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowedOrigins("http://localhost:5173")
                        .allowCredentials(true);
            }
        };
    }
}
