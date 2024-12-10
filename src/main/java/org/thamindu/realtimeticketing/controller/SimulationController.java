package org.thamindu.realtimeticketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.service.SimulationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:4200")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getSimulationStatus() {
        return ResponseEntity.ok(simulationService.isRunning());
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startSimulation() {
        try {
            Configuration config = Configuration.loadConfiguration();
            simulationService.startSimulation(config);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Simulation started successfully.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to load configuration."));
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stopSimulation() {
        simulationService.stopSimulation();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Simulation stopped.");
        return ResponseEntity.ok(response);
    }
}