package main.config;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code Configuration} class is the system configuration for the Real-Time Event Ticketing System. It includes parameters to set up the ticketing environment.
 *
 * <p>Parameters to configure:
 * <ul>
 *     <li>Total number of tickets available in the system</li>
 *     <li>Rate at which the tickets are released by vendors</li>
 *     <li>Rate at which the customers attempt to retrieve tickets</li>
 *     <li>Maximum capacity of tickets that can be held in at one time</li>
 * </ul>
 *
 * @author Thamindu Miuranda Hemachandra
 */
public class Configuration implements Serializable {

    /**
     * A unique identifier for this Serializable class.
     * Ensures compatibility during the serialization and deserialization processes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Default constructor.
     * Initializes the configuration with default values, ensures that the application can start without immediate user input. This is useful for testing, development, or fallback scenarios where no custom configurations provided.
     */
    public Configuration(){
        this.totalTickets = 100;
        this.ticketReleaseRate = 10;
        this.customerRetrievalRate = 5;
        this.maxTicketCapacity = 50;
    }

    /**
     * Configuration constructor.
     * Initializes the configuration.
     *
     * @param totalTickets Total number of tickets in the system
     * @param ticketReleaseRate Rate at which the tickets are released by vendors.
     * @param customerRetrievalRate Rate at which the customers attempt to retrieve the tickets.
     * @param maxTicketCapacity Maximum capacity of tickets in the pool.
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity =maxTicketCapacity;
    }

    /**
     * Gets the total number of tickets in the system.
     *
     * @return the total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number ot tickets in the system.
     *
     * @param totalTickets the total number of tickets.
     * @throws IllegalArgumentException if the value is negative.
     */
    public void setTotalTickets(int totalTickets) {
        if (totalTickets < 0){
            throw new IllegalArgumentException("Total tickets cannot be negative.");
        }
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the rate at which the tickets are released by the vendors.
     *
     * @return the ticket release rate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Set the rate at which the tickets are released by the vendors.
     *
     * @param ticketReleaseRate the ticket release rate.
     * @throws IllegalArgumentException if the value it negative.
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        if (ticketReleaseRate < 0){
            throw new IllegalArgumentException("Ticket release rate cannot be negative.");
        }
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the rate at which the customer attempt to retrieve tickets.
     *
     * @return the customer retrieval rate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Gets the rate at which the customer attempt to retrieve tickets.
     *
     * @param customerRetrievalRate the customer retrieval rate
     * @throws IllegalArgumentException if the value is negative.
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        if (customerRetrievalRate < 0){
            throw new IllegalArgumentException("Customer retrieval rate cannot be negative.");
        }
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of tickets in the pool.
     *
     * @return the maximum ticket capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum capacity of tickets in the pool.
     *
     * @param maxTicketCapacity the maximum ticket capacity.
     * @throws IllegalArgumentException if the value is negative or zero.
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (maxTicketCapacity < 0){
            throw new IllegalArgumentException("Maximum ticket capacity must be greater than zero.");
        }
        this.maxTicketCapacity = maxTicketCapacity;
    }

    @Override
    public String toString(){
        return "Configuration{" +
                "totalTickets=" + totalTickets + ", " +
                "ticketReleaseRate=" + ticketReleaseRate + "," +
                " customerRetrievalRate=" + customerRetrievalRate + "," +
                " maxTicketCapacity=" + maxTicketCapacity + "," +
                "}";
    }
}
