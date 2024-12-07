package com.W2051890.ticketing_system;

import com.W2051890.ticketing_system.model.Configuration;
import com.W2051890.ticketing_system.model.Customer;
import com.W2051890.ticketing_system.model.TicketPool;
import com.W2051890.ticketing_system.model.Vendor;
import com.W2051890.ticketing_system.service.InputService;
import com.W2051890.ticketing_system.util.LoggerUtil;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(TicketingSystemCLI.class, args);
    }
}
