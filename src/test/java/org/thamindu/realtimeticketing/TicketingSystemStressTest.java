package org.thamindu.realtimeticketing;

import org.junit.jupiter.api.Test;
import org.thamindu.realtimeticketing.model.Configuration;
import org.thamindu.realtimeticketing.model.TicketPool;
import org.thamindu.realtimeticketing.model.Vendor;
import org.thamindu.realtimeticketing.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TicketingSystemStressTest {

    @Test
    void stressTestWithModerateLoad() throws InterruptedException {
        Configuration config = new Configuration(1000, 50, 25, 500); // 1000 tickets, 50 tickets/sec, 25 customers/sec, pool capacity 500
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        ExecutorService executorService = Executors.newFixedThreadPool(100); // Moderate load: 50 vendors, 50 customers
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            Vendor vendor = new Vendor("Vendor-" + i, config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < 50; i++) {
            Customer customer = new Customer("Customer-" + i, config.getCustomerRetrievalRate(), 1000, ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS); // Allow threads 60 seconds to complete

        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        assert ticketPool.getTicketsAdded() == config.getTotalTickets();
        assert ticketPool.getTicketsSold() == config.getTotalTickets();
    }

    @Test
    void stressTestWithHeavyLoad() throws InterruptedException {
        Configuration config = new Configuration(5000, 100, 100, 1000); // 5000 tickets, 100 tickets/sec, 100 customers/sec
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        ExecutorService executorService = Executors.newFixedThreadPool(200); // Heavy load: 100 vendors, 100 customers
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Vendor vendor = new Vendor("Vendor-" + i, config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < 100; i++) {
            Customer customer = new Customer("Customer-" + i, config.getCustomerRetrievalRate(), 1000, ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        executorService.shutdown();
        executorService.awaitTermination(120, TimeUnit.SECONDS); // Allow threads 120 seconds to complete

        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        assert ticketPool.getTicketsAdded() == config.getTotalTickets();
        assert ticketPool.getTicketsSold() == config.getTotalTickets();
    }

    @Test
    void boundaryTestWithZeroTickets() throws InterruptedException {
        Configuration config = new Configuration(0, 10, 10, 10); // 0 tickets
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        ExecutorService executorService = Executors.newFixedThreadPool(20); // Minimal load: 10 vendors, 10 customers
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Vendor vendor = new Vendor("Vendor-" + i, config.getTicketReleaseRate(), ticketPool);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer("Customer-" + i, config.getCustomerRetrievalRate(), 1000, ticketPool);
            customers.add(customer);
            executorService.submit(customer);
        }

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS); // Allow threads 30 seconds to complete

        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        assert ticketPool.getTicketsAdded() == 0;
        assert ticketPool.getTicketsSold() == 0;
    }


}
