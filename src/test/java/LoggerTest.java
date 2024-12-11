import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A test class for verifying the logging functionality.
 * This class writes test log messages and verifies the existence and content of the log file.
 */
public class LoggerTest {

    /**
     * Logger instance for logging test-related events.
     */
    private static final Logger logger = LogManager.getLogger(LoggerTest.class);

    /**
     * Main method to test the logging functionality.
     * It writes test log messages and verifies the existence and content of the log file.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String logFilePath = "logs/application.log";

        // Write some test logs
        logger.info("Testing INFO log.");
        logger.warn("Testing WARN log.");
        logger.error("Testing ERROR log.");

        // Verify if the log file exists and contains logs
        try {
            final Path path = Paths.get(logFilePath);
            if (Files.exists(path)) {
                System.out.println("Log file found: " + logFilePath);
                String logContents = Files.readString(path);
                System.out.println("Log file contents:");
                System.out.println(logContents);
            } else {
                System.err.println("Log file not found: " + logFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }
}
