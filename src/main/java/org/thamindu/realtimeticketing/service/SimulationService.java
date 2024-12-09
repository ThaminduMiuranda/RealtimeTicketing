// src/main/java/org/thamindu/realtimeticketing/service/SimulationService.java

package org.thamindu.realtimeticketing.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SimulationService {

    private static final Logger logger = LogManager.getLogger(SimulationService.class);

    private ExecutorService executorService;
    private List<Vendor> vendors;
    private List<Customer> customers;
    private volatile boolean isRunning = false;

    private final SimpMessagingTemplate messagingTemplate;

    public SimulationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startSimulation(Configuration config) {
        if (isRunning) {
            logger.warn("Attempted to start simulation, but it's already running.");
            return;
        }
        isRunning = true;
        messagingTemplate.convertAndSend("/topic/simulation", "started");

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
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

        logger.info("Simulation started with configuration: {}", config);
    }

    public void stopSimulation() {
        if (!isRunning) {
            logger.warn("Attempted to stop simulation, but it's not running.");
            return;
        }
        isRunning = false;
        messagingTemplate.convertAndSend("/topic/simulation", "stopped");

        for (Vendor vendor : vendors) {
            vendor.stop();
        }
        for (Customer customer : customers) {
            customer.stop();
        }
        executorService.shutdown();
        logger.info("Simulation stopped.");
    }

    public boolean isRunning() {
        return isRunning;
    }
}