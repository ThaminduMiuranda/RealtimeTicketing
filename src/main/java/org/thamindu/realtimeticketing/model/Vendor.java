package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a Vendor in the ticketing system.
 * A Vendor is responsible for releasing tickets to the TicketPool at a specified rate.
 * <p>
 * This class is designed to be thread-safe and implements the {@code Runnable} interface
 * to allow concurrent execution of multiple vendors. The use of threads ensures scalability
 * for handling multiple ticket release operations.
 */
public class Vendor implements Runnable{

    /**
     * Logger instance for logging vendor activities.
     */
    private static final Logger logger = LogManager.getLogger(Vendor.class);

    /**
     * The unique identifier for the vendor.
     */
    private final String vendorId;
    /**
     * The rate at which the vendor releases tickets to the pool.
     */
    private final int ticketsReleaseRate;
    /**
     * The ticket pool to which the vendor releases tickets.
     */
    private final TicketPool ticketPool;
    /**
     * A volatile flag to safely manage the running state of the Vendor thread.
     * Volatile ensures visibility of changes across threads.
     */
    public volatile boolean isRunning = true;

    /**
     * Constructs a Vendor with the specified ID, ticket release rate, and ticket pool.
     *
     * @param vendorId the unique identifier for the vendor
     * @param ticketsReleaseRate the rate at which the vendor releases tickets
     * @param ticketPool the ticket pool to which the vendor releases tickets
     * @throws IllegalArgumentException if the ticket release rate is not positive
     */
    public Vendor(String vendorId, int ticketsReleaseRate, TicketPool ticketPool) {
        // Validate ticket release rate to ensure meaningful operations.
        if (ticketsReleaseRate <= 0){
            logger.error("Invalid ticketReleaseRate for Vendor: {}", ticketsReleaseRate);
            throw new IllegalArgumentException("Ticket release rate must be positive.");
        }

        this.vendorId = vendorId;
        this.ticketsReleaseRate = ticketsReleaseRate;
        this.ticketPool = ticketPool;

        logger.info("Vendor {} initialised with release rate: {}", vendorId,ticketsReleaseRate);
    }

    /**
     * Stops the vendor from releasing tickets by setting the running flag to false.
     * <p>
     * This method is thread-safe due to the use of the volatile variable {@code isRunning}.
     */
    public void stop(){
        isRunning = false;
    }

    /**
     * Runs the vendor, releasing tickets to the ticket pool at the specified rate.
     * The vendor stops running if interrupted or if the ticket pool is full.
     * <p>
     * This method ensures that ticket additions respect the ticket pool's capacity and
     * handles thread interruptions gracefully to avoid resource leaks.
     */
    @Override
    public void run(){
        logger.info("Vendor {} started.", vendorId);
        while (isRunning){
            try{
                for (int i = 0; i < ticketsReleaseRate; i++) {
                    String ticketBase = "Vendor-" + vendorId + "-Ticket";
                    // Attempt to add a ticket to the pool; stop if the pool is full.
                    if (!ticketPool.addTicket(ticketBase)){
                        logger.info("Vendor {} has completed ticket addition.", vendorId);
                        break;
                    }
                }
                // Pause between ticket releases to simulate real-time operations.
                Thread.sleep(1000);
            }catch (InterruptedException e){
                logger.error("Vendor {} interrupted.", vendorId);
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Vendor {} stopped.", vendorId);
    }
}
