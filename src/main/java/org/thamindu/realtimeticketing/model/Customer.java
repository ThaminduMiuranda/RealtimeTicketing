package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a Customer in the ticketing system.
 * A Customer retrieves tickets from the TicketPool at a specified rate and interval.
 *
 * <p>This class implements the {@code Runnable} interface to allow concurrent execution
 * of multiple customers. By simulating customers as separate threads, the system
 * can mimic real-world scenarios where multiple customers attempt to retrieve tickets
 * simultaneously.</p>
 *
 * <p><strong>Rationale:</strong> Implementing customers as threads allows for scalable
 * and realistic simulation of customer behavior in a concurrent environment.</p>
 */
public class Customer implements Runnable{

    /**
     * Logger instance for logging customer-related events.
     */
    private static final Logger logger = LogManager.getLogger(Customer.class);

    /**
     * The unique identifier for the customer.
     */
    private final String customerId;
    /**
     * The rate at which the customer retrieves tickets per interval.
     */
    private final int ticketRetrievalRate;
    /**
     * The interval (in milliseconds) between ticket retrieval attempts.
     */
    private final int retrievalInterval;
    /**
     * The ticket pool from which the customer retrieves tickets.
     */
    private final TicketPool ticketPool;
    /**
     * A volatile flag to safely manage the running state of the Customer thread.
     * Volatile ensures visibility of changes across threads.
     */
    public volatile boolean isRunning = true;

    /**
     * Constructs a Customer with the specified ID, ticket retrieval rate, retrieval interval, and ticket pool.
     *
     * @param customerId          the unique identifier for the customer
     * @param ticketRetrievalRate the rate at which the customer retrieves tickets per interval
     * @param retrievalInterval   the interval (in milliseconds) between retrieval attempts
     * @param ticketPool          the ticket pool from which the customer retrieves tickets
     * @throws IllegalArgumentException if the ticket retrieval rate is negative or retrieval interval is not positive
     *
     * <p><strong>Rationale:</strong> Parameters are validated to prevent invalid configurations
     * that could lead to unintended behavior or system crashes.</p>
     */
    public Customer(String customerId, int ticketRetrievalRate, int retrievalInterval, TicketPool ticketPool) {
        // Validate parameters to ensure meaningful operations
        if (ticketRetrievalRate < 0 || retrievalInterval <= 0){
            logger.error("Invalid parameters for customer: {}", ticketRetrievalRate);
            throw new IllegalArgumentException("TicketRetrievalRate and retrievalInterval must be positive");
        }
        this.customerId = customerId;
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;

        logger.info("Customer {} initialised with ticket retrieval rate {} and interval: {}", customerId, ticketRetrievalRate, retrievalInterval);
    }

    /**
     * Gets the customer ID.
     * @return the customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Gets the ticket retrieval rate.
     *
     * @return the ticket retrieval rate
     */
    public int getTicketRetrievalRate() {
        return ticketRetrievalRate;
    }

    /**
     * Gets the retrieval interval.
     *
     * @return the retrieval interval in milliseconds
     */
    public int getRetrievalInterval() {
        return retrievalInterval;
    }

    /**
     * Stops the customer from retrieving tickets by setting the running flag to false.
     *
     * <p><strong>Rationale:</strong> Using a volatile boolean allows for safe thread termination
     * without the risks associated with {@code Thread.stop()}, which is deprecated.</p>
     */
    public void stop(){
        isRunning = false;
    }

    /**
     * Runs the customer thread, attempting to retrieve tickets from the ticket pool
     * at the specified rate and interval.
     *
     * <p>This method simulates a customer making multiple ticket retrieval attempts,
     * pausing between each batch of attempts to mimic real-world behavior.</p>
     *
     * <p><strong>Rationale:</strong> The loop structure allows the customer to repeatedly
     * attempt ticket retrieval, and the sleep interval simulates delays between attempts,
     * adding realism to the simulation.</p>
     */
    @Override
    public void run() {
        logger.info("Customer {} started.", customerId);
        while (isRunning){
            try {
                for (int i = 0; i < ticketRetrievalRate; i++) {
                    // Attempt to remove a ticket from the ticket pool
                    String ticket = ticketPool.removeTicket();
                    if (ticket == null){
                        // No more tickets are available; exit the loop
                        break;
                    }
                    logger.info("Customer {} retrieved ticket: {}", customerId, ticket);
                }
                // Pause between retrieval attempts to simulate real-time operations
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e){
                logger.error("Customer {} interrupted.", customerId);
                Thread.currentThread().interrupt();
                break;
            }
            logger.info("Customer {} stopped.", customerId);
        }
    }
}
