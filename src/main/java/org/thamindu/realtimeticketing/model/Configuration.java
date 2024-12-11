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
 * Represents the configuration settings for the Real-Time Event Ticketing System.
 * This class encapsulates parameters such as total tickets, ticket release rate, customer retrieval rate,
 * and the maximum ticket pool capacity. The configuration can be loaded from or saved to a file for persistence.
 *
 * <p><strong>Rationale:</strong> Encapsulating system settings in a dedicated class allows
 * for easier management, reusability, and separation of concerns. Using JSON for configuration
 * ensures human-readable and modifiable settings.</p>
 */

public class Configuration implements Serializable {

    /**
     * A unique identifier for this Serializable class.
     * Ensures compatibility during the serialization and deserialization processes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default configuration file path for saving and loading settings.
     */
    private static final String DEFAULT_CONFIG_FILE = "config/system_config.json";

    /**
     * Customizable configuration file path via system properties.
     */
    private static final String CONFIG_FILE = System.getProperty("config.file.path", DEFAULT_CONFIG_FILE);

    /**
     * Logger instance for the Configuration class.
     * Used for logging configuration-related events and messages.
     */
    private static final Logger logger = LogManager.getLogger(Configuration.class);
    /**
     * Total tickets that will be added to the ticket pool
     */
    private int totalTickets;
    /**
     * The rate of tickets being added to the ticket pool
     */
    private int ticketReleaseRate;
    /**
     * The rate of tickets retrieved by customers
     */
    private int customerRetrievalRate;
    /**
     * Max capacity of the ticket pool
     */
    private int maxTicketCapacity;

    /**
     * Default constructor.
     * Initializes the configuration with default values, ensuring that the application can
     * start without requiring immediate user input.
     *
     * <p><strong>Rationale:</strong> Default values are essential for development, testing,
     * and fallback scenarios where no custom configurations are provided.</p>
     */
    public Configuration(){
        this.totalTickets = 100;
        this.ticketReleaseRate = 10;
        this.customerRetrievalRate = 5;
        this.maxTicketCapacity = 50;
    }

    /**
     * Parameterized constructor for creating a custom configuration.
     *
     * @param totalTickets        Total number of tickets in the system.
     * @param ticketReleaseRate   Rate at which vendors release tickets.
     * @param customerRetrievalRate The Rate at which customers retrieve tickets.
     * @param maxTicketCapacity   Maximum capacity of the ticket pool.
     *
     * <p><strong>Rationale:</strong> Allows initialization with specific values for flexibility
     * and testing various system scenarios.</p>
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity =maxTicketCapacity;
        logger.info("Configuration initialized with custom values.");
    }

    /**
     * Loads the configuration from the specified file path.
     *
     * @return the loaded Configuration object.
     * @throws IOException if the configuration file cannot be read.
     *
     * <p><strong>Rationale:</strong> Persistent configurations allow the system to maintain
     * state across sessions and simplify setup for recurring users.</p>
     */
    public static Configuration loadConfiguration() throws IOException {
        File file = new File(CONFIG_FILE);
        if (file.exists()){
            try (Reader reader = new FileReader(file)){
                Gson gson = new Gson();
                Configuration config = gson.fromJson(reader, Configuration.class);
//                logger.info("Configuration loaded from {}",CONFIG_FILE);
                return config;
            }
        } else {
            logger.warn("Configuration not found. Using the default configuration. ");
            Configuration defaultConfig = new Configuration();
            defaultConfig.saveConfiguration();
            return defaultConfig;
        }
    }

    /**
     * Saves the current configuration to the specified file path.
     */
    public void saveConfiguration(){
        try (Writer writer = new FileWriter(CONFIG_FILE)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this,writer);
            logger.info("Configuration saved to {}", CONFIG_FILE);
        }catch (IOException e){
            logger.error("Error saving the configuration: {}", e.getMessage());
        }
    }

    /**
     * Gets the total number of tickets in the system.
     *
     * @return the total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number of tickets in the system.
     *
     * @param totalTickets the total number of tickets.
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the rate at which vendors release tickets.
     *
     * @return the ticket release rate.
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Sets the rate at which vendors release tickets.
     *
     * @param ticketReleaseRate the ticket release rate.
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the rate at which customers retrieve tickets.
     *
     * @return the customer retrieval rate.
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Sets the rate at which customers retrieve tickets.
     *
     * @param customerRetrievalRate the customer retrieval rate.
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of the ticket pool.
     *
     * @return the maximum ticket pool capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum capacity of the ticket pool.
     *
     * @param maxTicketCapacity the maximum ticket pool capacity.
     */
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
