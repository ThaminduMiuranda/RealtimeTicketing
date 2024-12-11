package org.thamindu.realtimeticketing.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.model.TicketPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing ticket-related operations.
 * This controller provides endpoints to retrieve the status of the ticketing system,
 * including the total tickets, tickets sold, and tickets available.
 *
 * <p><strong>Rationale:</strong> By exposing ticket-related data through RESTful endpoints,
 * this class enables seamless integration between the backend and frontend applications,
 * allowing real-time status updates for users.</p>
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    /**
     * The ticket pool used in the ticketing system.
     *
     * <p><strong>Rationale:</strong> The ticket pool is a shared resource that tracks the total tickets,
     * tickets sold, and tickets available. It is injected into the controller to provide real-time
     * status updates to the frontend application.</p>
     */
    private TicketPool ticketPool;

    /**
     * Constructs a TicketController with the specified ticket pool.
     *
     * @param ticketPool the shared ticket pool used to track tickets.
     *
     * <p><strong>Rationale:</strong> Dependency injection ensures better testability
     * and decouples the controller from the ticket pool's implementation details.</p>
     */
    @Autowired
    public TicketController(TicketPool ticketPool) throws IOException {
        this.ticketPool = ticketPool;
    }

    /**
     * Retrieves the status of the ticketing system, including total tickets, tickets sold, and tickets available.
     *
     * @return a {@code ResponseEntity} containing a map of ticket-related status data.
     * If an error occurs while loading the configuration, a 500 status is returned.
     *
     * <p><strong>Rationale:</strong> Centralized status retrieval enables frontend systems to
     * display real-time information to users, improving transparency and user experience.</p>
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Integer>> getTicketStatus() throws IOException {
        // Load the configuration to fetch system-wide settings.
        Configuration config = Configuration.loadConfiguration();
//        logger.info("TicketPool instance in TicketController: {}", ticketPool.hashCode());
//        logger.info("Fetching ticket status...");

        // Prepare the ticket status response.
        Map<String, Integer> status = new HashMap<>();
        status.put("totalTickets", config != null ? config.getTotalTickets():0);
        status.put("ticketsSold", ticketPool != null ? ticketPool.getTicketsSold() : 0);
        status.put("ticketsAvailable", ticketPool != null ? ticketPool.getAvailableTickets() : 0);
//        logger.info("Ticket status: {}", status);
        return ResponseEntity.ok(status);
    }
}
