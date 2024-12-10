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

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private static final Logger logger = LogManager.getLogger(TicketController.class);
    private TicketPool ticketPool;


    @Autowired
    public TicketController(TicketPool ticketPool) throws IOException {
        this.ticketPool = ticketPool;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Integer>> getTicketStatus() throws IOException {
        Configuration config = Configuration.loadConfiguration();
//        logger.info("TicketPool instance in TicketController: {}", ticketPool.hashCode());
//        logger.info("Fetching ticket status...");
        Map<String, Integer> status = new HashMap<>();
        status.put("totalTickets", config != null ? config.getTotalTickets():0);
        status.put("ticketsSold", ticketPool != null ? ticketPool.getTicketsSold() : 0);
        status.put("ticketsAvailable", ticketPool != null ? ticketPool.getAvailableTickets() : 0);
//        logger.info("Ticket status: {}", status);
        return ResponseEntity.ok(status);
    }
}
