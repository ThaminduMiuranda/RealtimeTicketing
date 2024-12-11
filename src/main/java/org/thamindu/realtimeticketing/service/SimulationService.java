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

/**
 * Manages the ticketing simulation by coordinating vendors, customers, and the ticket pool.
 * This service is responsible for starting and stopping the simulation, as well as maintaining its state.
 *
 * <p><strong>Rationale:</strong> Encapsulating simulation logic in a dedicated service class
 * ensures separation of concerns and provides a centralized point for managing simulation-related tasks.</p>
 */
@Service
public class SimulationService {

    /**
     * Logger instance for logging simulation events and errors.
     */
    private static final Logger logger = LogManager.getLogger(SimulationService.class);

    /**
     * The ticket pool used in the simulation.
     *
     * <p><strong>Rationale:</strong> The ticket pool is a shared resource among vendors and customers,
     * representing the available tickets in the simulation. It is initialized based on the configuration
     * and is accessed concurrently by multiple threads.</p>
     */
    private TicketPool ticketPool;

    /**
     * The thread pool for managing vendor and customer threads.
     *
     * <p><strong>Rationale:</strong> Using an ExecutorService allows for efficient management
     * of concurrent tasks, ensuring that vendor and customer threads are properly handled
     * and resources are utilized effectively.</p>
     */
    private ExecutorService executorService; // Thread pool for managing vendor and customer threads.

    /**
     * List of active vendor threads.
     */
    private List<Vendor> vendors; // List of active vendor threads.
    /**
     * List of active customer threads.
     */
    private List<Customer> customers; // List of active customer threads.
    /**
     * Flag to track whether the simulation is running.
     *
     * <p><strong>Rationale:</strong> This flag is used to ensure that the simulation
     * is not started or stopped multiple times concurrently, maintaining the integrity
     * of the simulation state.</p>
     */
    private volatile boolean isRunning = false; // Flag to track whether the simulation is running.

    /**
     * Constructs a SimulationService with a specified ticket pool.
     *
     * @param ticketPool the shared ticket pool to be used in the simulation.
     *
     * <p><strong>Rationale:</strong> Injecting the ticket pool as a dependency
     * ensures better testability and loose coupling.</p>
     */
    @Autowired
    public SimulationService( TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Starts the ticketing simulation by initializing vendor and customer threads
     * based on the provided configuration.
     *
     * @param config the configuration for the simulation.
     *
     * <p><strong>Rationale:</strong> Initializing threads dynamically based on the configuration
     * allows for flexibility in testing different scenarios.</p>
     */
    public void startSimulation(Configuration config) {
        if (isRunning) {
            logger.warn("Attempted to start simulation, but it's already running.");
            return;
        }
        isRunning = true;

        // Load the configuration and initialize the ticket pool.
        try {
            Configuration newConfig = Configuration.loadConfiguration();
            ticketPool.initialize(newConfig.getMaxTicketCapacity(), newConfig.getTotalTickets());
            config = newConfig; // Update the passed config object to match the loaded configuration.
        } catch (IOException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            stopSimulation();
            return;
        }
        logger.info("TicketPool reinitialized: Max Capacity = {}, Total Tickets = {}", config.getMaxTicketCapacity(), config.getTotalTickets());

        ticketPool.initialize(config.getMaxTicketCapacity(), config.getTotalTickets());

        // Determine the number of vendor and customer threads.
        int numVendorThreads = Math.max(1, config.getTotalTickets() / config.getTicketReleaseRate());
        int numCustomerThreads = Math.max(1, config.getTotalTickets() / config.getCustomerRetrievalRate());
        executorService = Executors.newFixedThreadPool(numVendorThreads + numCustomerThreads);

        vendors = new ArrayList<>();
        customers = new ArrayList<>();

        // Initialize vendor threads.
        for (int i = 0; i < numVendorThreads; i++) {
            Vendor vendor = new Vendor("Vendor-" + (i + 1), config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor); // Submit vendor to the thread pool.
        }

        // Initialize customer threads.
        for (int i = 0; i < numCustomerThreads; i++) {
            Customer customer = new Customer("Customer-" + (i + 1), config.getCustomerRetrievalRate(), 1000, ticketPool);
            customers.add(customer);
            executorService.submit(customer); // Submit customer to the thread pool.
        }
//        logger.info("TicketPool instance in SimulationService: {}", ticketPool.hashCode());
        logger.info("Simulation started with configuration: {}", config);
    }

    /**
     * Stops the ticketing simulation by terminating all vendor and customer threads
     * and shutting down the thread pool.
     *
     * <p><strong>Rationale:</strong> Graceful shutdown ensures all resources are released
     * properly and prevents potential memory leaks.</p>
     */
    public void stopSimulation() {
        if (!isRunning) {
            logger.warn("Attempted to stop simulation, but it's not running.");
            return;
        }
        isRunning = false;

        // Stop all vendor threads.
        for (Vendor vendor : vendors) {
            vendor.stop();
        }
        // Stop all customer threads.
        for (Customer customer : customers) {
            customer.stop();
        }

        // Shut down the ticket pool.
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

    /**
     * Checks whether the simulation is currently running.
     *
     * @return true if the simulation is running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
}