package service;

import util.LoggerUtil;

import java.util.Scanner;

public class InputService {

    private final Scanner scanner;

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
                    LoggerUtil.warn("Input must be between "+min+" and "+max+".");
                }
            }catch (NumberFormatException e){
                LoggerUtil.error("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
