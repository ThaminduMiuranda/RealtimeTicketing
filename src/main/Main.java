package main;

import main.config.Configuration;
import main.model.Customer;
import main.model.TicketPool;
import main.model.Vendor;
import main.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool(200);
        int numVendorThreads = 5;
        int numCustomerThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numVendorThreads + numCustomerThreads);

        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < numVendorThreads; i++) {
            String id = Integer.toString(i);
            /*
            Here the created Vendor runnable is wrapped around a thread. But executorService.submit() already accepts Runnable. So no need to wrap it with new Thread.
            executorService.submit(new Thread (new Vendor(id,3,ticketPool)));
            so the correct way be,
             */
            Vendor vendor = new Vendor(id,3,ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < numCustomerThreads; i++) {
            String id = Integer.toString(i);
            Customer customer = new Customer(id,4,1000,ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            LoggerUtil.error("Main thread interrupted during sleep: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        for(Vendor vendor : vendors){
            vendor.stop();
        }

        for (Customer customer : customers){
            customer.stop();
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Forcing shutdown as tasks did not finish in time...");
                executorService.shutdownNow();
            } else {
                System.out.println("All vendor threads completed successfully.");
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted: " + e.getMessage());
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
