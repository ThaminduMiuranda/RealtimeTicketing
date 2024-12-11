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
 * The {@code TicketPool} class is the ticket queue, which is a shared resource across the Real-Time Event Ticketing
 * System where tickets are stored.
 * <p> It supports thread safe synchronized operations for adding and removing tickets while preventing
 * race conditions. This class ensures data integrity in the multithreaded environment by synchronizing critical methods.
 *
 * @author Thamindu Miuranda
 *
 */
@Component
@Scope("singleton")
public class TicketPool {

    private static final Logger logger = LogManager.getLogger(TicketPool.class);

    private List<String> tickets;

    @Value("${ticket.pool.totalTickets:100}")
    private int totalTickets;
    private int ticketsAdded = 0;
    private int ticketsSold = 0;
    private int availableTickets = totalTickets;


    @Value("${ticket.pool.maxCapacity:50}")
    private int maxCapacity;
    private Semaphore ticketsAvailable;
    private Semaphore spaceAvailable;


    private volatile boolean isStopped = false;

    public void stopSimulation(){
        isStopped = true;
    }

    private static TicketPool instance;

    /**
     * Constructs a {@code TicketPool} with a specific maximum capacity
     *
     * @param maxCapacity the maximum number of tickets that can be held in the pool.
     * @throws IllegalArgumentException if the maximum capacity is less than or equal to zero.
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
        this.ticketsAvailable = new Semaphore(0); //initially no tickets available
        this.spaceAvailable = new Semaphore(maxCapacity); //initially, all space is available
        logger.info("TicketPool instance created");
    }



    /**
     * Adds tickets to the pool.
     *
     * @param ticketBase the ticket to be added.
     * @throws IllegalStateException if adding the ticket exceeds the maximum capacity of the ticket pool.
     */
    public boolean addTicket(String ticketBase){
        try{
            //waits for space become available.
            spaceAvailable.acquire();

            synchronized (this){
                if (isStopped) return false;
                if (ticketsAdded >= totalTickets){
                    spaceAvailable.release(); // Release the permit back
                    return true;
                }
                if (tickets.size() >= maxCapacity){
                    logger.info("Max capacity reached. Waiting...");
                    return false;
                }
                String ticketId = ticketBase + "-" + ticketsAdded;
                tickets.add(ticketId);
                ticketsAdded++;
                logger.info("Ticket added: {} (Total added: {})", ticketId, ticketsAdded);
            }
            ticketsAvailable.release();
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
     * Removes the first ticket from the ticket pool.
     *
     * @return the ticket removed from the pool.
     * @throws IllegalStateException if the pool is empty.
     */
    public String removeTicket(){
        try{
            //waits for space become available.
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
                ticket = tickets.removeFirst(); // Remove ticket from the queue.
                ticketsSold++;

                if (availableTickets > 0){
                    availableTickets --;
                }
                if (ticket != null) {
                    logger.info("Ticket removed: {}", ticket);
                }

            }
            //Signals that space is now available
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

    public int getTotalTickets() {
        return totalTickets; // Return the `totalTickets` field from your class
    }

    public int getTicketsSold() {
        return ticketsSold; // Return the number of tickets sold
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

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
