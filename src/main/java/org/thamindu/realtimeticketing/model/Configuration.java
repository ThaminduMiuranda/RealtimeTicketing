package org.thamindu.realtimeticketing.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thamindu.realtimeticketing.util.LoggerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * The {@code Configuration} class is the system configuration for the Real-Time Event Ticketing System. It includes parameters to set up the ticketing environment.
 *
 * <p>Parameters to configure:
 * <ul>
 *     <li>Total number of tickets available in the system</li>
 *     <li>Rate at which the tickets are released by vendors</li>
 *     <li>Rate at which the customers attempt to retrieve tickets</li>
 *     <li>Maximum capacity of tickets that can be held in at one time</li>
 * </ul>
 *
 * @author Thamindu Miuranda Hemachandra
 */

public class Configuration implements Serializable {

    /**
     * A unique identifier for this Serializable class.
     * Ensures compatibility during the serialization and deserialization processes.
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CONFIG_FILE = "config/system_config.json";
    private static final String CONFIG_FILE = System.getProperty("config.file.path", DEFAULT_CONFIG_FILE);


    private static final Logger logger = LogManager.getLogger(Configuration.class);

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Default constructor.
     * Initializes the configuration with default values, ensures that the application can start without immediate user input. This is useful for testing, development, or fallback scenarios where no custom configurations provided.
     */
    public Configuration(){
        this.totalTickets = 100;
        this.ticketReleaseRate = 10;
        this.customerRetrievalRate = 5;
        this.maxTicketCapacity = 50;
    }

    /**
     * Configuration constructor.
     * Initializes the configuration.
     *
     * @param totalTickets Total number of tickets in the system
     * @param ticketReleaseRate Rate at which the tickets are released by vendors.
     * @param customerRetrievalRate The Rate at which the customers attempt to retrieve the tickets.
     * @param maxTicketCapacity Maximum capacity of tickets in the pool.
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity =maxTicketCapacity;
        logger.info("Configuration initialized with custom values.");
    }

    public static Configuration loadConfiguration() throws IOException {
        File file = new File(CONFIG_FILE);
        if (file.exists()){
            try (Reader reader = new FileReader(file)){
                Gson gson = new Gson();
                Configuration config = gson.fromJson(reader, Configuration.class);
                logger.info("Configuration loaded from {}",CONFIG_FILE);
                return config;
            }
        } else {
            logger.warn("Configuration not found. Using the default configuration. ");
            Configuration defaultConfig = new Configuration();
            defaultConfig.saveConfiguration();
            return defaultConfig;
        }
    }

    public void saveConfiguration(){
        try (Writer writer = new FileWriter(CONFIG_FILE)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this,writer);
            logger.info("Configuration saved to {}", CONFIG_FILE);
        }catch (IOException e){
            logger.error("Error saving the configuration: {}", e.getMessage());
        }
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    @Override
    public String toString(){
        return "Configuration{" +
                "totalTickets=" + totalTickets + ", " +
                "ticketReleaseRate=" + ticketReleaseRate + "," +
                " customerRetrievalRate=" + customerRetrievalRate + "," +
                " maxTicketCapacity=" + maxTicketCapacity + "," +
                "}";
    }
}
