package org.thamindu.realtimeticketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 * This class defines the global CORS settings to allow secure communication
 * between the frontend and backend when hosted on different origins.
 *
 * <p><strong>Rationale:</strong> Proper CORS configuration is essential for enabling
 * secure interactions between the Angular frontend and Spring Boot backend,
 * while preventing unauthorized access from other domains.</p>
 */
@Configuration
public class CorsConfig {

    /**
     * Defines a {@code WebMvcConfigurer} bean to customize the CORS settings for the application.
     *
     * @return a {@code WebMvcConfigurer} instance with specified CORS configurations.
     *
     * <p><strong>Rationale:</strong> Using a centralized CORS configuration ensures consistency
     * and reduces the risk of misconfigurations across individual endpoints.</p>
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow all endpoints to be accessed from the specified origin.
                registry.addMapping("/**") // Applies to all endpoints in the application.
                        .allowedOrigins("http://localhost:4200") // Restrict access to the specified origin.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Allow these HTTP methods.
            }
        };
    }
}
