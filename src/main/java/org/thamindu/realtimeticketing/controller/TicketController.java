package org.thamindu.realtimeticketing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thamindu.realtimeticketing.model.TicketPool;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketPool ticketPool;

    public TicketController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Integer>> getTicketStatus() {
        Map<String, Integer> status = new HashMap<>();
        status.put("totalTickets", ticketPool.getTotalTickets());
        status.put("ticketsSold", ticketPool.getTicketsSold());
        status.put("ticketsAvailable", ticketPool.getCurrentSize());
        return ResponseEntity.ok(status);
    }
}
