package model;

import util.LoggerUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private int totalTickets;
    private int ticketsAdded;
    private int ticketsSold;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();


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
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.ticketsAdded = 0;
        this.ticketsSold = 0;
        LoggerUtil.info("TicketPool initialised with max capacity: " + maxCapacity);
    }

    /**
     * Adds tickets to the pool.
     *
     * @param ticket the ticket to be added.
     * @throws IllegalStateException if adding the ticket exceeds the maximum capacity of the ticket pool.
     */
    public boolean addTicket(String ticket){
        lock.lock();
        try{
            if (ticketsAdded >= totalTickets){
                return false;
            }
            while (tickets.size() >= maxCapacity){
                LoggerUtil.warn("Cannot add ticket. Pool has reached its maximum capacity. Waiting...");
                notFull.await();
            }
            tickets.add(ticket);
            ticketsAdded++;
            LoggerUtil.info("Ticket added: "+ticket);
            notEmpty.signalAll(); //Notify the waiting threads to remove tickets
            return true;
        } catch (InterruptedException e){
            LoggerUtil.error("Thread interrupted while waiting to add tickets.");
            Thread.currentThread().interrupt();
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes the first ticket from the ticket pool.
     *
     * @return the ticket removed from the pool.
     * @throws IllegalStateException if the pool is empty.
     */
    public String removeTicket(){
        lock.lock();

        try{
            while (tickets.isEmpty()) {
                LoggerUtil.warn("Cannot retrieve tickets. Pool is empty. Waiting...");
                notEmpty.await();

            }
            String ticket = tickets.removeFirst();
            ticketsSold++;
            LoggerUtil.info("Ticket removed: " + ticket);
            notFull.signalAll(); // Notify any waiting threads to add tickets.
            return ticket;
        } catch (InterruptedException e){
            LoggerUtil.error("Thread interrupted while waiting to remove ticket.");
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }

    }

    /**
     * Gets the number of tickets currently in the pool.
     *
     * @return the number of tickets currently in the pool.
     */
    public int getCurrentSize() {
        lock.lock();
        try{
            int size = tickets.size();
            LoggerUtil.info("Current pool size queried: " + size);
            return size;
        } finally {
            lock.unlock();
        }
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
        lock.lock();
        try{
            return (ticketsAdded >= totalTickets && ticketsSold >= ticketsAdded);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString(){
        lock.lock();
        try{
            return "TicketPool{" +
                    "tickets=" + tickets +
                    ", maxCapacity=" + maxCapacity +
                    "}";
        } finally {
            lock.unlock();
        }
    }



}
