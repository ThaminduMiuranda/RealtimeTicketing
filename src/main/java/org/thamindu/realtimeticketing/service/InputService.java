package org.thamindu.realtimeticketing.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thamindu.realtimeticketing.util.LoggerUtil;

import java.util.Scanner;

/**
 * A service class for managing user input in the ticketing system.
 * This class provides methods for validated input retrieval, ensuring that
 * users provide valid and consistent data during configuration.
 *
 * <p><strong>Rationale:</strong> Centralizing input validation logic reduces code duplication,
 * improves maintainability, and enhances the user experience by guiding them to correct input.</p>
 */
public class InputService {
    /**
     * Scanner instance for reading user input.
     */
    private final Scanner scanner; // Scanner for reading user input.

    /**
     * Logger instance for logging input service-related events.
     */
    private static final Logger logger = LogManager.getLogger(InputService.class);

    /**
     * Constructs an InputService with the specified scanner.
     *
     * @param scanner the {@code Scanner} instance for reading user input.
     *
     * <p><strong>Rationale:</strong> Dependency injection of the scanner allows for easier
     * testing and ensures flexibility in input sources (e.g., console, file).</p>
     */
    public InputService(Scanner scanner){
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter a valid integer within the specified range.
     *
     * @param prompt the message to display to the user.
     * @param min    the minimum valid value (inclusive).
     * @param max    the maximum valid value (inclusive).
     * @return a valid integer input from the user.
     *
     * <p><strong>Rationale:</strong> Validating user input ensures data integrity and
     * prevents runtime errors caused by invalid or unexpected input.</p>
     */
    public int getValidInteger(String prompt, int min, int max){
        int input;
        while (true){
            System.out.println(prompt); // Display the prompt to the user.
            try{
                // Parse and validate the user input.
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max){
                    return input; // Return the valid input.
                }else {
                    // Log a warning if the input is out of range.
                    logger.warn("Input must be between {} and {}.", min, max);
                }
            }catch (NumberFormatException e){
                // Log an error if the input is not a valid integer.
                logger.error("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
