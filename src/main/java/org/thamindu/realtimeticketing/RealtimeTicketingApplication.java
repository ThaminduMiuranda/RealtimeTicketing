package org.thamindu.realtimeticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Real-Time Event Ticketing System application.
 * This class initializes and starts the Spring Boot application, enabling
 * backend functionality and exposing REST endpoints for the ticketing system.
 *
 * <p><strong>Rationale:</strong> Using Spring Boot provides a streamlined way to manage dependencies,
 * configure components, and handle requests with minimal boilerplate, making it an ideal choice
 * for building RESTful backend services.</p>
 */
@SpringBootApplication
public class RealtimeTicketingApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args command-line arguments passed to the application.
     *
     * <p><strong>Rationale:</strong> The {@code SpringApplication.run} method simplifies application
     * startup by handling component scanning, dependency injection, and embedded server initialization.</p>
     */
    public static void main(String[] args) {
        SpringApplication.run(RealtimeTicketingApplication.class, args);
    }

}
