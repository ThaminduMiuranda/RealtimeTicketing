package org.thamindu.realtimeticketing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.service.SimulationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing the ticketing simulation.
 * This controller provides endpoints to start, stop, and check the status of the simulation.
 *
 * <p><strong>Rationale:</strong> Exposing simulation control through REST APIs allows the frontend
 * to dynamically interact with the backend, providing a seamless user experience for managing
 * the ticketing simulation in real-time.</p>
 */
@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:4200")
public class SimulationController {

    /**
     * The service responsible for managing the ticketing simulation.
     */
    private final SimulationService simulationService;
    /**
     * Constructs a SimulationController with the specified simulation service.
     *
     * @param simulationService the service responsible for managing the ticketing simulation.
     *
     * <p><strong>Rationale:</strong> Dependency injection ensures that the controller remains decoupled
     * from the simulation logic, improving testability and maintainability.</p>
     */
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }
    /**
     * Retrieves the current status of the simulation.
     *
     * @return a {@code ResponseEntity} containing a boolean value indicating whether the simulation is running.
     *
     * <p><strong>Rationale:</strong> Providing a status endpoint allows the frontend to poll the server
     * for updates, ensuring accurate and real-time feedback for users.</p>
     */
    @GetMapping("/status")
    public ResponseEntity<Boolean> getSimulationStatus() {
        // Return the current running state of the simulation.
        return ResponseEntity.ok(simulationService.isRunning());
    }

    /**
     * Starts the ticketing simulation using the configuration loaded from file.
     *
     * @return a {@code ResponseEntity} containing a message indicating the outcome of the operation.
     * If an error occurs while loading the configuration, a 500 status is returned.
     *
     * <p><strong>Rationale:</strong> Allowing the frontend to start the simulation dynamically
     * improves the flexibility and control of the system, enabling real-time scenario testing.</p>
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startSimulation() {
        try {
            // Load the configuration and start the simulation.
            Configuration config = Configuration.loadConfiguration();
            simulationService.startSimulation(config);

            // Prepare a success response.
            Map<String, String> response = new HashMap<>();
            response.put("message", "Simulation started successfully.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Log the error and return a failure response.
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to load configuration."));
        }
    }

    /**
     * Stops the ticketing simulation.
     *
     * @return a {@code ResponseEntity} containing a message indicating that the simulation has been stopped.
     *
     * <p><strong>Rationale:</strong> Providing a stop endpoint ensures graceful termination of the simulation,
     * preventing resource leaks and maintaining system stability.</p>
     */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stopSimulation() {
        // Stop the simulation and prepare a success response.
        simulationService.stopSimulation();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Simulation stopped.");
        return ResponseEntity.ok(response);
    }
}