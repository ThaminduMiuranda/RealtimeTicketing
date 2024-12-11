package org.thamindu.realtimeticketing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket communication.
 * This class sets up the WebSocket message broker and endpoint configuration,
 * enabling real-time communication between the server and clients.
 *
 * <p><strong>Rationale:</strong> WebSocket integration is essential for real-time features in the
 * ticketing system, such as instant updates and notifications for users, enhancing user experience.</p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for WebSocket communication.
     *
     * @param config the {@code MessageBrokerRegistry} used to configure the message broker.
     *
     * <p><strong>Rationale:</strong> Using a message broker ensures scalability and organizes
     * communication channels between the server and clients efficiently.</p>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker for topic-based messaging.
        config.enableSimpleBroker("/topic"); // Prefix for destinations the broker will handle.

        // Set application-level destination prefix for messages bound for annotated methods.
        config.setApplicationDestinationPrefixes("/app"); // Prefix for server-side message handling.
    }

    /**
     * Registers STOMP (Simple Text Oriented Messaging Protocol) endpoints for WebSocket connections.
     *
     * @param registry the {@code StompEndpointRegistry} used to register endpoints.
     *
     * <p><strong>Rationale:</strong> Registering WebSocket endpoints enables clients to connect
     * to the server and facilitates cross-origin communication with specific frontend origins.</p>
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint and allow cross-origin requests.
        registry.addEndpoint("/ws") // WebSocket endpoint for client connections.
                .setAllowedOrigins("http://localhost:4200") // Restrict connections to the frontend origin.
                .withSockJS(); // Enable fallback options for browsers that don't support WebSocket.
    }
}