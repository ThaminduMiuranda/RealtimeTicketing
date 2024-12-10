import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoggerTest {
    private static final Logger logger = LogManager.getLogger(LoggerTest.class);

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
