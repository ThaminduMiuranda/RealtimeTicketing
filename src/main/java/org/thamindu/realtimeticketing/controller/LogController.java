package org.thamindu.realtimeticketing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing and retrieving application logs.
 * This controller provides an endpoint to retrieve filtered logs, enabling monitoring
 * and debugging of the system in a structured manner.
 *
 * <p><strong>Rationale:</strong> Centralized log retrieval allows administrators and developers
 * to analyze system behavior without directly accessing server files, improving accessibility and security.</p>
 */
@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "http://localhost:4200")
public class LogController {

    /**
     * Path to the application's log file, configurable via application properties.
     * Default value is {@code logs/application.log}.
     */
    @Value("${logging.file.name:logs/application.log}")
    private String logFilePath;

    /**
     * Retrieves filtered logs from the application log file.
     *
     * @return a {@code ResponseEntity} containing a list of log entries filtered by relevant packages.
     * If an error occurs while reading the log file, a 500 status is returned.
     *
     * <p><strong>Rationale:</strong> Filtering logs by specific packages ensures that only relevant
     * entries are returned, reducing noise and making debugging more efficient.</p>
     */
    @GetMapping
    public ResponseEntity<List<String>> getLogs() {
        try {
            // Read all lines from the log file.
            List<String> logs = Files.readAllLines(Paths.get(logFilePath));

            // Filter logs to include only those related to specified packages.
            List<String> filteredLogs = logs.stream()
                    .filter(log ->
                            log.contains("org.thamindu.realtimeticketing.model") ||
                            log.contains("org.thamindu.realtimeticketing.service") ||
                            log.contains("org.thamindu.realtimeticketing.controller"))
                    .collect(Collectors.toList());

            // Return the filtered logs as the response.
            return ResponseEntity.ok(filteredLogs);
        } catch (IOException e) {
            // Log the exception and return a server error response.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}