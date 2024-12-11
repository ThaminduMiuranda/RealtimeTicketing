package org.thamindu.realtimeticketing.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thamindu.realtimeticketing.util.LoggerUtil;

import java.util.Scanner;

public class InputService {

    private final Scanner scanner;

    private static final Logger logger = LogManager.getLogger(InputService.class);

    public InputService(Scanner scanner){
        this.scanner = scanner;
    }

    public int getValidInteger(String prompt, int min, int max){
        int input;
        while (true){
            System.out.println(prompt);
            try{
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max){
                    return input;
                }else {
                    logger.warn("Input must be between {} and {}.", min, max);
                }
            }catch (NumberFormatException e){
                logger.error("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
