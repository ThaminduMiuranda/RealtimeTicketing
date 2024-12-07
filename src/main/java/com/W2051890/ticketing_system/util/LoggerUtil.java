package com.W2051890.ticketing_system.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code LoggerUtil} class provides the logging functionality for the system.
 *
 * <p>This utility supports logging messages to the console as well as to a log file.
 * It is designed to record activities like ticket additions, removals and system errors in a structured format.
 *
 * <p>Key features:
 * <ul>
 *     <li>Thread-safe logging for concurrent operations.</li>
 *     <li>Logs include timestamps for better traceability.</li>
 * </ul>
 *
 */
public class LoggerUtil {

    private static final String LOG_FILE = "system.log";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message to both the console and a log file.
     *
     * <p>This method is synchronized to ensure thread-safe logging in concurrent environments.
     * It formats the log entry with a timestamp, log level, and message, then outputs it to the console
     * and appends it to a log file.
     *
     * @param level the log level (e.g., INFO, WARN, ERROR)
     * @param message the message to be logged
     */
    public static synchronized void log(String level, String message){
        String timeStamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String logEntry = String.format("[%s] [%s] %s", timeStamp, level, message);

        //Logs to console
        System.out.println(logEntry);

        //Log to file
        /*
          try-with-resources statement
           - to prevent memory leaks and to make the application use fewer resources.
         */
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    /**
     * Logs a normal informative message.
     *
     * @param message the message to be logged.
     */
    public static void info(String message) {
        log("INFO", message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the message to be logged.
     */
    public static void warn(String message){
        log("WARN", message);
    }

    /**
     * Logs an error message.
     *
     * @param message the message to be logged.
     */
    public static void error(String message){
        log("ERROR", message);
    }
}
