package main.model;

import main.util.LoggerUtil;

public class Vendor implements Runnable{

    private final String vendorId;
    private final int ticketsReleaseRate;
    private final TicketPool ticketPool;
    public volatile boolean isRunning = true;

    public Vendor(String vendorId, int ticketsReleaseRate, TicketPool ticketPool) {

        if (ticketsReleaseRate <= 0){
            LoggerUtil.error("Invalid ticketReleaseRate for Vendor: " + ticketsReleaseRate);
            throw new IllegalArgumentException("Ticket release rate must be positive.");
        }

        this.vendorId = vendorId;
        this.ticketsReleaseRate = ticketsReleaseRate;
        this.ticketPool = ticketPool;

        LoggerUtil.info("Vendor "+ vendorId + " initialized with release rate: "+ticketsReleaseRate);
    }

    public void stop(){
        isRunning = false;
    }

    @Override
    public void run(){
        LoggerUtil.info("Vendor "+ vendorId + " started.");
        while (isRunning) {
            try {
                for (int i = 0; i < ticketsReleaseRate; i++) {
                    String ticketId = "Vendor-" + vendorId + "-Ticket-" + System.currentTimeMillis();
                    ticketPool.addTicket(ticketId);
                }
                Thread.sleep(1000);
            } catch (IllegalStateException e) {
                LoggerUtil.warn("Vendeor " + vendorId + ": " + e.getMessage());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    LoggerUtil.error("Vendor " + vendorId + " interrupted while waiting.");
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (InterruptedException e){
                LoggerUtil.error("Vendor "+vendorId+" interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        LoggerUtil.info("Vendor "+vendorId+" stopped.");

    }
}
