package org.thamindu.realtimeticketing.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thamindu.realtimeticketing.model.Configuration;

import java.io.IOException;

@RestController
@RequestMapping("/api/configuration")
@CrossOrigin(origins = "http://locathost:4200")
public class ConfigurationController {

    private Configuration currentConfiguration = new Configuration();

    @GetMapping
    public ResponseEntity<Configuration> getConfiguration(){
        try {
            Configuration currentConfiguration = Configuration.loadConfiguration();
            return ResponseEntity.ok(currentConfiguration);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<Configuration> saveConfiguration(@RequestBody Configuration configuration){
        currentConfiguration = configuration;
        configuration.saveConfiguration();
        return ResponseEntity.ok(currentConfiguration);
    }

}
