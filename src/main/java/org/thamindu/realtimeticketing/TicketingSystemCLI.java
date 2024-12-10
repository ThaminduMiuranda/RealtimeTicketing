package org.thamindu.realtimeticketing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.model.Customer;
import org.thamindu.realtimeticketing.model.TicketPool;
import org.thamindu.realtimeticketing.model.Vendor;
import org.thamindu.realtimeticketing.service.InputService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TicketingSystemCLI {

    private static final Logger logger = LogManager.getLogger(TicketingSystemCLI.class);

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        Configuration config = Configuration.loadConfiguration();
        if (!new File("system_config.json").exists()){
            System.out.println("No configuration file found.");
            System.out.println("Do you want to create a new configuration? (yes/no):");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")|| response.equals("y")){
                config = configureSystem();
                config.saveConfiguration();
            }
        } else {
            System.out.println("Configuration file found. Do you want to use the existing configuration? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("no") || response.equals("n")) {
                System.out.println("Do you want to create a new configuration? (yes/no):");
                String secondResponse = scanner.nextLine().trim().toLowerCase();
                if (secondResponse.equals("yes") || secondResponse.equals("y")){
                    config = configureSystem();
                    config.saveConfiguration();
                }
            }
        }

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        int numVendorThreads = Math.max(1,config.getTotalTickets()/config.getTicketReleaseRate());
        int numCustomerThreads = Math.max(1, config.getTotalTickets()/config.getCustomerRetrievalRate());
        ExecutorService executorService = Executors.newFixedThreadPool(numVendorThreads + numCustomerThreads);
        int retrievalInterval = 1000;

        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

//        LoggerUtil.info(config.toString());
        logger.info(config.toString());

        for (int i = 0; i < numVendorThreads; i++) {
            String id = Integer.toString(i);
            /*
            Here the created Vendor runnable is wrapped around a thread. But executorService.submit() already accepts Runnable. So no need to wrap it with new Thread.
            executorService.submit(new Thread (new Vendor(id,3,ticketPool)));
            so the correct way be,
             */
            Vendor vendor = new Vendor("Vendor-"+id, config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < numCustomerThreads; i++) {
            String id = Integer.toString(i);
            Customer customer = new Customer("Customer-"+id, config.getCustomerRetrievalRate(), retrievalInterval,ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        while (!ticketPool.isSimulationComplete()){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
//                LoggerUtil.error("com.W2051890.ticketing_system.Main thread interrupted.");
                logger.error("com.W2051890.ticketing_system.Main thread interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

//        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.error("Executor service did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

//        if (executorService.isShutdown()){
//
//        }
            logger.info("Simulation complete. All tickets added and sold.");

//        LoggerUtil.info("Simulation complete. All tickets added and sold.");
    }


    public static Configuration configureSystem(){
        Scanner scanner = new Scanner(System.in);
        InputService inputService = new InputService(scanner);

        System.out.println("Welcome to the Real-Time Event Ticketing System!");
        System.out.println("Please provide following details to configure the system: ");

        int totalTickets = inputService.getValidInteger("Total number of tickets (must be greater than 0): ", 1, Integer.MAX_VALUE);
        int ticketReleaseRate = inputService.getValidInteger("Ticket release rate (must be greater than 0): ", 1, Integer.MAX_VALUE);
        int customerRetrievalRate = inputService.getValidInteger("Customer retrieval rate (must be greater than 0): ",1, Integer.MAX_VALUE);
        int maxTicketCapacity = inputService.getValidInteger("Maximum ticket capacity (must be greater than 0): ",1,Integer.MAX_VALUE);

        return new Configuration(totalTickets,ticketReleaseRate,customerRetrievalRate,maxTicketCapacity);
    }
}
