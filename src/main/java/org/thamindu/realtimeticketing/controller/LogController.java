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

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "http://localhost:4200")
public class LogController {

    @Value("${logging.file.name:logs/application.log}")
    private String logFilePath;

    @GetMapping
    public ResponseEntity<List<String>> getLogs() {
        try {
            List<String> logs = Files.readAllLines(Paths.get(logFilePath));

            List<String> filteredLogs = logs.stream()
                    .filter(log ->
                            log.contains("org.thamindu.realtimeticketing.model") ||
                            log.contains("org.thamindu.realtimeticketing.service") ||
                            log.contains("org.thamindu.realtimeticketing.controller"))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredLogs);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}