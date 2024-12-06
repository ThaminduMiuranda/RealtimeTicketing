package main.model;

import main.util.LoggerUtil;

public class Customer implements Runnable{

    private final String customerId;
    private final int ticketRetrievalRate;
    private final int retrievalInterval;
    private final TicketPool ticketPool;
    public volatile boolean isRunning = true;

    public Customer(String customerId, int ticketRetrievalRate, int retrievalInterval, TicketPool ticketPool) {
        if (ticketRetrievalRate < 0 || retrievalInterval <= 0){
            LoggerUtil.error("Invalid parameters for customer: "+ticketRetrievalRate);
            throw new IllegalArgumentException("TicketRetrievalRate and retrievalInterval must be positive");
        }
        this.customerId = customerId;
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;

        LoggerUtil.info("Customer "+customerId+" initialised with ticket retrieval rate "+ticketRetrievalRate+" and interval: "+retrievalInterval);
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
        LoggerUtil.info("Customer "+customerId+" started.");
        while (isRunning){
            try {
                for (int i = 0; i < ticketRetrievalRate; i++) {
                    try {
                        String ticket = ticketPool.removeTicket();
                        LoggerUtil.info("Customer " + customerId + " retrieved ticket: " + ticket);
                    } catch (IllegalStateException e) {
                        LoggerUtil.warn("Customer " + customerId + ": " + e.getMessage());
                        break;
                    }
                }
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e){
                LoggerUtil.error("Customer "+customerId+" interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
            LoggerUtil.info("Customer "+customerId+" stopped.");
        }
    }
}
