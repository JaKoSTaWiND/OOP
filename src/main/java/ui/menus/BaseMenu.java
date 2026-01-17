package ui.menus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import exceptions.InvalidInputException;

public abstract class BaseMenu {
    protected final Scanner scanner;

    public BaseMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    protected String readString(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new InvalidInputException("Field cannot be empty!");
        }
        return input;
    }

    protected int readInt(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine());
            if (value < 0) throw new InvalidInputException("Value cannot be negative!");
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid format! Please enter a whole number.");
        }
    }

    protected BigDecimal readBigDecimal(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        try {
            BigDecimal value = new BigDecimal(scanner.nextLine());
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidInputException("Price cannot be negative!");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid price format! Use numbers (e.g., 10.50).");
        }
    }

    protected double readDouble(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        try {
            double value = Double.parseDouble(scanner.nextLine());
            if (value < 0) throw new InvalidInputException("Value cannot be negative!");
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid format! Please enter a number (e.g., 0.2).");
        }
    }

    protected LocalDate readLocalDate(String prompt) throws InvalidInputException {
        System.out.print(prompt + " (YYYY-MM-DD): ");
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format! Use YYYY-MM-DD (e.g., 2024-01-15).");
        }
    }
}