// src/main/java/org/thamindu/realtimeticketing/service/SimulationService.java

package org.thamindu.realtimeticketing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.model.Customer;
import org.thamindu.realtimeticketing.model.TicketPool;
import org.thamindu.realtimeticketing.model.Vendor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SimulationService {

    private static final Logger logger = LogManager.getLogger(SimulationService.class);

//    @Autowired
    private TicketPool ticketPool;

    private ExecutorService executorService;
    private List<Vendor> vendors;
    private List<Customer> customers;
    private volatile boolean isRunning = false;

//    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SimulationService( TicketPool ticketPool) {
//        this.messagingTemplate = messagingTemplate;
        this.ticketPool = ticketPool;
    }

    public void startSimulation(Configuration config) {
        if (isRunning) {
            logger.warn("Attempted to start simulation, but it's already running.");
            return;
        }
        isRunning = true;

        // Reload configuration explicitly
        try {
            Configuration newConfig = Configuration.loadConfiguration();
            ticketPool.initialize(newConfig.getMaxTicketCapacity(), newConfig.getTotalTickets());
            config = newConfig; // Update the passed config object
        } catch (IOException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            stopSimulation();
            return;
        }
        logger.info("TicketPool reinitialized: Max Capacity = {}, Total Tickets = {}", config.getMaxTicketCapacity(), config.getTotalTickets());
//        messagingTemplate.convertAndSend("/topic/simulation", "started");

        ticketPool.initialize(config.getMaxTicketCapacity(), config.getTotalTickets());
        int numVendorThreads = Math.max(1, config.getTotalTickets() / config.getTicketReleaseRate());
        int numCustomerThreads = Math.max(1, config.getTotalTickets() / config.getCustomerRetrievalRate());
        executorService = Executors.newFixedThreadPool(numVendorThreads + numCustomerThreads);

        vendors = new ArrayList<>();
        customers = new ArrayList<>();

        for (int i = 0; i < numVendorThreads; i++) {
            Vendor vendor = new Vendor("Vendor-" + (i + 1), config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < numCustomerThreads; i++) {
            Customer customer = new Customer("Customer-" + (i + 1), config.getCustomerRetrievalRate(), 1000, ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }
//        logger.info("TicketPool instance in SimulationService: {}", ticketPool.hashCode());
        logger.info("Simulation started with configuration: {}", config);
    }

    public void stopSimulation() {
        if (!isRunning) {
            logger.warn("Attempted to stop simulation, but it's not running.");
            return;
        }
        isRunning = false;
//        messagingTemplate.convertAndSend("/topic/simulation", "stopped");

        for (Vendor vendor : vendors) {
            vendor.stop();
        }
        for (Customer customer : customers) {
            customer.stop();
        }

        ticketPool.stopSimulation();
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e){
            logger.error("Error while shutting down executor service.",e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("Simulation stopped.");
    }

    public boolean isRunning() {
        return isRunning;
    }
}