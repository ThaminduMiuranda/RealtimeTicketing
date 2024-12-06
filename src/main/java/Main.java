import config.Configuration;
import model.Customer;
import model.TicketPool;
import model.Vendor;
import util.LoggerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Configuration config = Configuration.loadConfiguration();
        if (!new File("system_config.json").exists()){
            System.out.println("No configuration file found.");
            System.out.println("Do you want to create a new configuration? (yes/no):");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")){
                config = configureSystem();
                config.saveConfiguration();
            }
        } else {
            System.out.println("Configuration file found. Do you want to use the existing configuration? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("no")) {
                System.out.println("Do you want to create a new configuration? (yes/no):");
                String secondResponse = scanner.nextLine().trim().toLowerCase();
                if (secondResponse.equals("yes")){
                    config = configureSystem();
                    config.saveConfiguration();
                }
            }
        }

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        int numVendorThreads = Math.floorDiv(config.getTotalTickets(), config.getTicketReleaseRate());
        int numCustomerThreads = Math.floorDiv(config.getTotalTickets(), config.getCustomerRetrievalRate());
        ExecutorService executorService = Executors.newFixedThreadPool(numVendorThreads + numCustomerThreads);

        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        LoggerUtil.info(config.toString());

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
            Customer customer = new Customer("Customer-"+id, config.getCustomerRetrievalRate(), 1000,ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        while (!ticketPool.isSimulationComplete()){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                LoggerUtil.error("Main thread interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            LoggerUtil.error("Main thread interrupted during sleep: " + e.getMessage());
//            Thread.currentThread().interrupt();
//        }

        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        executorService.shutdown();

        LoggerUtil.info("Simulation complete. All tickets added and sold.");

//        try {
//            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
//                System.out.println("Forcing shutdown as tasks did not finish in time...");
//                executorService.shutdownNow();
//            } else {
//                System.out.println("All vendor threads completed successfully.");
//            }
//        } catch (InterruptedException e) {
//            System.err.println("Shutdown interrupted: " + e.getMessage());
//            executorService.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
    }


    public static Configuration configureSystem(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Real-Time Event Ticketing System!");
        System.out.println("Please provide following details to configure the system: ");

        System.out.println("Total number of tickets: ");
        int totalTickets = scanner.nextInt();

        System.out.println("Ticket release rate: ");
        int ticketReleaseRate = scanner.nextInt();

        System.out.println("Customer retrieval rate: ");
        int customerRetrievalRate = scanner.nextInt();

        System.out.println("Maximum ticket capacity: ");
        int maxTicketCapacity = scanner.nextInt();

        return new Configuration(totalTickets,ticketReleaseRate,customerRetrievalRate,maxTicketCapacity);
    }
}
