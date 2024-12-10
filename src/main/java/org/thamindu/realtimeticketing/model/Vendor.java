package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.thamindu.realtimeticketing.util.LoggerUtil;

/**
 * Represents a Vendor in the ticketing system.
 * A Vendor is responsible for releasing tickets to the TicketPool at a specified rate.
 */
public class Vendor implements Runnable{

    private static final Logger logger = LogManager.getLogger(Vendor.class);

    private final String vendorId;
    private final int ticketsReleaseRate;
    private final TicketPool ticketPool;
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

        if (ticketsReleaseRate <= 0){
//            LoggerUtil.error("Invalid ticketReleaseRate for Vendor: " + ticketsReleaseRate);
            logger.error("Invalid ticketReleaseRate for Vendor: {}", ticketsReleaseRate);
            throw new IllegalArgumentException("Ticket release rate must be positive.");
        }

        this.vendorId = vendorId;
        this.ticketsReleaseRate = ticketsReleaseRate;
        this.ticketPool = ticketPool;

//        LoggerUtil.info("Vendor "+ vendorId + " initialized with release rate: "+ticketsReleaseRate);
        logger.info("Vendor {} initialised with release rate: {}", vendorId,ticketsReleaseRate);
    }

    /**
     * Stops the vendor from releasing tickets.
     */
    public void stop(){
        isRunning = false;
    }

    /**
     * Runs the vendor, releasing tickets to the ticket pool at the specified rate.
     * The vendor stops running if interrupted or if the ticket pool is full.
     */
    @Override
    public void run(){
//        LoggerUtil.info("Vendor "+ vendorId + " started.");
        logger.info("Vendor {} started.", vendorId);
        while (isRunning){
            try{
                for (int i = 0; i < ticketsReleaseRate; i++) {
                    String ticketBase = "Vendor-" + vendorId + "-Ticket";
                    if (!ticketPool.addTicket(ticketBase)){
//                        LoggerUtil.info("Vendor "+vendorId+" has completed ticket addition.");
                        logger.info("Vendor {} has completed ticket addition.", vendorId);
//                        stop();
                        break;
                    }
                }
                Thread.sleep(1000);
            }catch (InterruptedException e){
//                LoggerUtil.error("Vendor "+vendorId+" interrupted.");
                logger.error("Vendor {} interrupted.", vendorId);
                Thread.currentThread().interrupt();
                break;
            }
        }
//        LoggerUtil.info("Vendor "+vendorId+" stopped.");
        logger.info("Vendor {} stopped.", vendorId);
    }
}
