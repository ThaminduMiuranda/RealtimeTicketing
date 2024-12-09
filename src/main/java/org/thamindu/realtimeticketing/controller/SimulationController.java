package org.thamindu.realtimeticketing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.service.SimulationService;

import java.io.IOException;

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
    public ResponseEntity<String> startSimulation() {
        try {
            Configuration config = Configuration.loadConfiguration();
            simulationService.startSimulation(config);
            return ResponseEntity.ok("Simulation started successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to load configuration.");
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation stopped successfully.");
    }
}