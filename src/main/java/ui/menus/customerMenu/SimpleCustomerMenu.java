package ui.menus.customerMenu;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.customerServices.SimpleCustomerService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class SimpleCustomerMenu extends BaseMenu implements Menu {
    private final SimpleCustomerService simpleCustomerService;

    public SimpleCustomerMenu(SimpleCustomerService simpleCustomerService, Scanner scanner) {
        super(scanner);
        this.simpleCustomerService = simpleCustomerService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- CUSTOMERS MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Customers
            2. Add New Customer
            3. Add Loyalty Points


            0. Back
            """);
        System.out.print("Choice > ");
    }

    @Override
    public void run() {
        boolean back = false;
        while (!back) {
            displayOptions();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> TableRenderer.printCustomerTable(simpleCustomerService.getAllCustomers());
                    case "2" -> {
                        int id = readInt("Enter Customer ID: ");
                        String name = readString("Enter Name: ");
                        String phone = readString("Enter Phone: ");
                        int loyaltyPoints = readInt("Enter Loyalty Points: ");
                        simpleCustomerService.addCustomer(id, name, phone, loyaltyPoints, false);
                    }
                    case "3" -> {
                        int id = readInt("Enter Customer ID: ");
                        double amount = readDouble("Enter Amount to Add: ");
                        simpleCustomerService.addLoyaltyPoints(id, amount);
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());

            }
        }
    }
}
