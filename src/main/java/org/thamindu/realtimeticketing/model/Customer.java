package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thamindu.realtimeticketing.util.LoggerUtil;

public class Customer implements Runnable{

    private static final Logger logger = LogManager.getLogger(Customer.class);

    private final String customerId;
    private final int ticketRetrievalRate;
    private final int retrievalInterval;
    private final TicketPool ticketPool;
    public volatile boolean isRunning = true;

    public Customer(String customerId, int ticketRetrievalRate, int retrievalInterval, TicketPool ticketPool) {
        if (ticketRetrievalRate < 0 || retrievalInterval <= 0){
//            LoggerUtil.error("Invalid parameters for customer: "+ticketRetrievalRate);
            logger.error("Invalid parameters for customer: {}", ticketRetrievalRate);
            throw new IllegalArgumentException("TicketRetrievalRate and retrievalInterval must be positive");
        }
        this.customerId = customerId;
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;

//        LoggerUtil.info("Customer "+customerId+" initialised with ticket retrieval rate "+ticketRetrievalRate+" and interval: "+retrievalInterval);
        logger.info("Customer {} initialised with ticket retrieval rate {} and interval: {}", customerId, ticketRetrievalRate, retrievalInterval);
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getTicketRetrievalRate() {
        return ticketRetrievalRate;
    }

    public int getRetrievalInterval() {
        return retrievalInterval;
    }

    public void stop(){
        isRunning = false;
    }

    @Override
    public void run() {
//        LoggerUtil.info("Customer "+customerId+" started.");
        logger.info("Customer {} started.", customerId);
        while (isRunning){
            try {
                for (int i = 0; i < ticketRetrievalRate; i++) {
                    String ticket = ticketPool.removeTicket();
                    if (ticket == null){
                        stop();
                        break;
                    }
//                    LoggerUtil.info("Customer " + customerId + " retrieved ticket: " + ticket);
                    logger.info("Customer {} retrieved ticket: {}", customerId, ticket);
                }
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e){
//                LoggerUtil.error("Customer "+customerId+" interrupted.");
                logger.error("Customer {} interrupted.", customerId);
                Thread.currentThread().interrupt();
                break;
            }
//            LoggerUtil.info("Customer "+customerId+" stopped.");
            logger.info("Customer {} stopped.", customerId);
        }
    }
}
