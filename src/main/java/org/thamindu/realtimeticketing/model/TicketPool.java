package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Represents the shared ticket pool in the Real-Time Event Ticketing System.
 * This class acts as a synchronized queue where tickets are added by vendors and retrieved by customers.
 *
 * <p><strong>Rationale:</strong> A shared resource like a ticket pool requires thread-safe operations
 * to ensure data integrity in a concurrent environment. Semaphores and synchronized blocks are
 * used to manage access and prevent race conditions.</p>
 */
@Component
@Scope("singleton")
public class TicketPool {

    /**
     * Logger instance for logging events in the TicketPool.
     */
    private static final Logger logger = LogManager.getLogger(TicketPool.class);
    /**
     * A thread-safe list to store tickets in the pool.
     */
    private List<String> tickets;
    /**
     * The total number of tickets to be processed.
     * This value is injected from the application properties.
     */
    @Value("${ticket.pool.totalTickets:100}")
    private int totalTickets; // Total number of tickets to be processed.
    /**
     * The maximum capacity of the pool at any given time.
     * This value is injected from the application properties.
     */
    @Value("${ticket.pool.maxCapacity:50}")
    private int maxCapacity; // Maximum capacity of the pool at any given time.
    /**
     * The number of tickets available to be added to the pool.
     */
    private int ticketsAdded = 0; // Counter for the total number of tickets added.
    /**
     * The number of tickets available to be added to the pool.
     */
    private int ticketsSold = 0; // Counter for the total number of tickets retrieved (sold).
    /**
     * The number of tickets available to be added to the pool.
     */
    private int availableTickets = totalTickets; // The Number of tickets remaining to be added.
    /**
     * Semaphore to track available tickets for retrieval.
     */
    private Semaphore ticketsAvailable; // Semaphore to track available tickets for retrieval.
    /**
     * Semaphore to track available space for adding tickets.
     */
    private Semaphore spaceAvailable; // Semaphore to track available space for adding tickets.
    /**
     * Semaphore to track available tickets for retrieval.
     */
    private volatile boolean isStopped = false; // Flag to manage simulation state.

    /**
     * Stops the simulation by setting the stopped flag to true.
     *
     * <p><strong>Rationale:</strong> A volatile flag ensures consistent visibility across threads,
     * allowing for safe termination of operations.</p>
     */
    public void stopSimulation(){
        isStopped = true;
    }

    /**
     * Singleton instance of TicketPool.
     */
    private static TicketPool instance;

    /**
     * Constructs a TicketPool with a specified maximum capacity and total tickets.
     *
     * @param maxCapacity the maximum number of tickets that can be held in the pool at a time.
     * @param totalTickets the total number of tickets to be added to the pool.
     * @throws IllegalArgumentException if the maximum capacity is less than or equal to zero.
     *
     * <p><strong>Rationale:</strong> Constructor ensures proper initialization of critical
     * fields and prevents invalid configurations.</p>
     */
    @Autowired
    public TicketPool(@Value("${ticket.pool.maxCapacity:50}") int maxCapacity, @Value("${ticket.pool.totalTickets:100}") int totalTickets){
        if (maxCapacity <= 0) {
            logger.error("Invalid maximum capacity: {}", maxCapacity);
            throw new IllegalArgumentException("Max capacity must be greater than zero.");
        }
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;

        // Initialize semaphores to control ticket pool access.
        this.ticketsAvailable = new Semaphore(0); //initially no tickets available
        this.spaceAvailable = new Semaphore(maxCapacity); //initially, all space is available
        logger.info("TicketPool instance created");
    }

    /**
     * Adds a ticket to the pool.
     *
     * @param ticketBase the base identifier for the ticket.
     * @return true if the ticket was successfully added, false otherwise.
     *
     * <p><strong>Rationale:</strong> Synchronization ensures thread-safe ticket addition,
     * while semaphores prevent overfilling the pool.</p>
     */
    public boolean addTicket(String ticketBase){
        try{
            //waits for space become available.
            spaceAvailable.acquire();

            synchronized (this){
                if (isStopped) return false; // Exit if the simulation has been stopped.
                if (ticketsAdded >= totalTickets){
                    spaceAvailable.release(); // Release the permit back if no more tickets can be added.
                    return true;
                }
                if (tickets.size() >= maxCapacity){
                    logger.info("Max capacity reached. Waiting...");
                    return false;
                }
                // Generate a unique ticket ID and add it to the pool.
                String ticketId = ticketBase + "-" + ticketsAdded;
                tickets.add(ticketId);
                ticketsAdded++;
                logger.info("Ticket added: {} (Total added: {})", ticketId, ticketsAdded);
            }
            ticketsAvailable.release(); // Signal that a ticket is available for retrieval.
            if (!isStopped){
                Thread.sleep(1000);
            }
            return true;
        } catch (InterruptedException e){
            logger.error("Thread interrupted while waiting to add tickets while waiting to add.");
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Removes a ticket from the pool.
     *
     * @return the removed ticket, or null if the pool is empty or stopped.
     *
     * <p><strong>Rationale:</strong> Synchronization ensures thread-safe ticket retrieval,
     * while semaphores prevent retrieving from an empty pool.</p>
     */
    public String removeTicket(){
        try{
            // Wait for a ticket to become available in the pool.
            ticketsAvailable.acquire();

            if (isStopped) return null;

            if (ticketsAdded >= totalTickets){
                ticketsAvailable.release(); // Release the permit back
            }
            String ticket;
            synchronized (this){
                if (tickets.isEmpty()){
                    logger.info("Ticket pool is empty. Waiting...");
                }
                ticket = tickets.removeFirst(); // Remove and return the first ticket in the queue.
                ticketsSold++;

                if (availableTickets > 0){
                    availableTickets --;
                }
                if (ticket != null) {
                    logger.info("Ticket removed: {}", ticket);
                }

            }
            // Signal that space is now available in the pool.
            spaceAvailable.release();
            if (!isStopped){
                Thread.sleep(1000);
            }
            return ticket;
        } catch (InterruptedException e){
            logger.error("Thread interrupted while waiting to add tickets while waiting to remove.");
            Thread.currentThread().interrupt();
            return null;
        }

    }

    /**
     * Returns the total number of tickets.
     *
     * @return the total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets; // Return the `totalTickets` field from your class
    }

    /**
     * Returns the number of tickets sold.
     *
     * @return the number of tickets sold.
     */
    public int getTicketsSold() {
        return ticketsSold; // Return the number of tickets sold
    }

    /**
     * Returns the number of available tickets.
     *
     * @return the number of available tickets.
     */
    public int getAvailableTickets() {
        return availableTickets;
    }

    /**
     * Sets the number of available tickets.
     *
     * @param availableTickets the number of available tickets to set.
     */
    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    /**
     * Returns the number of tickets added to the pool.
     *
     * @return the number of tickets added.
     */
    public int getTicketsAdded() {
        return ticketsAdded;
    }

    /**
     * Gets the number of tickets currently in the pool.
     *
     * @return the number of tickets currently in the pool.
     */
    public int getCurrentSize() {
        int size = tickets.size();
//        logger.info("Current pool size queried: {}", size);
        return size;
    }

    /**
     * Gets the maximum capacity of the ticket pool.
     *
     * @return the maximum capacity of the pool.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Checks if the simulation is complete.
     *
     * @return true if all tickets have been added and the pool is empty, false otherwise.
     */
    public boolean isSimulationComplete(){
        synchronized (this){
            return (ticketsAdded >= totalTickets && tickets.isEmpty());
        }
    }

    @Override
    public String toString(){
        return "TicketPool{" +
                "tickets=" + tickets +
                ", maxCapacity=" + maxCapacity +
                "}";
    }

    /**
     * Reinitialized the TicketPool with new parameters.
     *
     * @param maxCapacity the new maximum capacity of the pool.
     * @param totalTickets the new total number of tickets.
     */
    public void initialize(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.tickets.clear();
        this.ticketsAdded = 0;
        this.ticketsSold = 0;
        this.availableTickets = totalTickets;
        this.ticketsAvailable = new Semaphore(0);
        this.spaceAvailable = new Semaphore(maxCapacity);
        this.isStopped = false;
//        logger.info("TicketPool reinitialized: Max Capacity = {}, Total Tickets = {}", maxCapacity, totalTickets);
    }
}
