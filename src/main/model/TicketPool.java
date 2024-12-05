package main.model;

import main.util.LoggerUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    private final List<String> tickets;
    private final int maxCapacity;


    /**
     * Constructs a {@code TicketPool} with a specific maximum capacity
     *
     * @param maxCapacity the maximum number of tickets that can be held in the pool.
     * @throws IllegalArgumentException if the maximum capacity is less than or equal to zero.
     */
    public TicketPool(int maxCapacity){
        if (maxCapacity <= 0) {
            LoggerUtil.error("Invalid maximum capacity: " + maxCapacity);
            throw new IllegalArgumentException("Max capacity must be greater than zero.");
        }
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxCapacity = maxCapacity;
        LoggerUtil.info("TicketPool initialised with max capacity: " + maxCapacity);
    }

    /**
     * Adds tickets to the pool.
     *
     * @param ticket the ticket to be added.
     * @throws IllegalStateException if adding the ticket exceeds the maximum capacity of the ticket pool.
     */
    public synchronized void addTicket(String ticket){
        if (tickets.size() >= maxCapacity){
            try {
                LoggerUtil.warn("Cannot add ticket. Pool has reached its maximum capacity. Waiting...");
                wait();
            } catch (InterruptedException e) {
                LoggerUtil.error("Thread interrupted while waiting to add tickets.");
                Thread.currentThread().interrupt();
            }
        }
        tickets.add(ticket);
        LoggerUtil.info("Ticket added: " + ticket);
        notifyAll(); //Notify any waiting threads that space is now available.
    }

    /**
     * Removes the first ticket from the ticket pool.
     *
     * @return the ticket removed from the pool.
     * @throws IllegalStateException if the pool is empty.
     */
    public synchronized String removeTicket(){
        if (tickets.isEmpty()){
            try{
                LoggerUtil.warn("Cannot retrieve tickets. Pool is empty. Waiting...");
                wait();
            } catch (InterruptedException e){
                LoggerUtil.error("Thread interrupted while waiting to remove ticket.");
                Thread.currentThread().interrupt();
            }
        }
        String ticket = tickets.removeFirst();
        LoggerUtil.info("Ticket removed: " + ticket);
        notifyAll(); // Notify any waiting threads that a ticket has been removed.
        return ticket;
    }

    /**
     * Gets the number of tickets currently in the pool.
     *
     * @return the number of tickets currently in the pool.
     */
    public synchronized int getCurrentSize() {
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

    @Override
    public synchronized String toString(){
        return "TicketPool{" +
                "tickets=" + tickets +
                ", maxCapacity=" + maxCapacity +
                "}";
    }



}
