package org.thamindu.realtimeticketing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thamindu.realtimeticketing.model.Configuration;

/**
 * REST controller for managing the system configuration.
 * This controller provides endpoints to retrieve and update the application's configuration settings,
 * enabling dynamic adjustments without modifying the codebase.
 *
 * <p><strong>Rationale:</strong> Exposing configuration management through REST APIs enhances flexibility,
 * allowing administrators to update settings at runtime and improving overall system usability.</p>
 */
@RestController
@RequestMapping("/api/configuration")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfigurationController {

    /**
     * Retrieves the current configuration of the system.
     *
     * @return a {@code ResponseEntity} containing the current {@code Configuration} object.
     * If an error occurs during retrieval, a 500 status is returned.
     *
     * <p><strong>Rationale:</strong> Providing a retrieval endpoint enables administrators to
     * view the current system settings dynamically without accessing backend files.</p>
     */
    @GetMapping
    public ResponseEntity<Configuration> getConfiguration(){
        try {
            // Load the configuration from the predefined file.
            Configuration currentConfiguration = Configuration.loadConfiguration();
            return ResponseEntity.ok(currentConfiguration);
        }catch (Exception e){
            // Log the error and return a server error response.
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Updates and saves the system configuration with the provided settings.
     *
     * @param configuration the new configuration to save.
     * @return a {@code ResponseEntity} containing the saved {@code Configuration} object.
     * If an error occurs during saving, a 500 status is returned.
     *
     * <p><strong>Rationale:</strong> Allowing dynamic updates to the configuration promotes
     * flexibility and reduces the need for redeployment when changes are required.</p>
     */
    @PostMapping
    public ResponseEntity<Configuration> saveConfiguration(@RequestBody Configuration configuration){
        try {
            // Save the provided configuration to the predefined file.
            configuration.saveConfiguration();
            return ResponseEntity.ok(configuration);
        } catch (Exception e) {
            // Log the error and return a server error response.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
