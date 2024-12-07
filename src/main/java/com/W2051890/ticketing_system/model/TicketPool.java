package com.W2051890.ticketing_system.model;

import com.W2051890.ticketing_system.util.LoggerUtil;

import java.util.LinkedList;
import java.util.Queue;
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
public class TicketPool {

    private final Queue<String> tickets;
    private final int maxCapacity;
    private final Semaphore ticketsAvailable;
    private final Semaphore spaceAvailable;

    private final int totalTickets;
    private int ticketsAdded = 0;

    /**
     * Constructs a {@code TicketPool} with a specific maximum capacity
     *
     * @param maxCapacity the maximum number of tickets that can be held in the pool.
     * @throws IllegalArgumentException if the maximum capacity is less than or equal to zero.
     */
    public TicketPool(int maxCapacity, int totalTickets){
        if (maxCapacity <= 0) {
            LoggerUtil.error("Invalid maximum capacity: " + maxCapacity);
            throw new IllegalArgumentException("Max capacity must be greater than zero.");
        }
        this.tickets = new LinkedList<>();
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.ticketsAvailable = new Semaphore(0); //initially no tickets available
        this.spaceAvailable = new Semaphore(maxCapacity); //initially, all space is available
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
                if (ticketsAdded >= totalTickets){
                    spaceAvailable.release(); // Release the permit back
                    return true;
                }
                if (tickets.size() >= maxCapacity){
                    LoggerUtil.info("Max capacity reached. Waiting...");
                    return false;
                }
                String ticketId = ticketBase + "-" + ticketsAdded;
                tickets.add(ticketId);
                ticketsAdded++;
                LoggerUtil.info("Ticket added: "+ticketId+" (Total added: "+ ticketsAdded+")");
            }
            ticketsAvailable.release();
            return true;
        } catch (InterruptedException e){
            LoggerUtil.error("Thread interrupted while waiting to add tickets.");
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

            if (ticketsAdded >= totalTickets){
                ticketsAvailable.release(); // Release the permit back
            }
            String ticket;
            synchronized (this){
                if (tickets.isEmpty()){
                    LoggerUtil.info("Ticket pool is empty. Waiting...");
                }
                ticket = tickets.poll(); // Remove ticket from the queue.
                if (ticket != null) {
                    LoggerUtil.info("Ticket removed: " + ticket);
                }

            }
            //Signals that space is now available
            spaceAvailable.release();
            return ticket;
        } catch (InterruptedException e){
            LoggerUtil.error("Thread interrupted while waiting to add tickets.");
            Thread.currentThread().interrupt();
            return null;
        }

    }

    /**
     * Gets the number of tickets currently in the pool.
     *
     * @return the number of tickets currently in the pool.
     */
    public int getCurrentSize() {
        int size = tickets.size();
        LoggerUtil.info("Current pool size queried: " + size);
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



}
