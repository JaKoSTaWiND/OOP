package ui.menus.customerMenu;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.CustomerFactory;
import interfaces.ICustomerService;
import interfaces.Menu;
import models.Customer;
import ui.TableRenderer;
import ui.menus.BaseMenu;

@Component
public class SimpleCustomerMenu extends BaseMenu implements Menu {
    private final ICustomerService customerService;

    public SimpleCustomerMenu(ICustomerService customerService, Scanner scanner) {
        super(scanner);
        this.customerService = customerService;
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
                    case "1" -> TableRenderer.printCustomerTable(customerService.getAllCustomers());
                    case "2" -> {
                        int id = readInt("Enter Customer ID: ");
                        String name = readString("Enter Name: ");
                        String phone = readString("Enter Phone: ");
                        int loyaltyPoints = readInt("Enter Loyalty Points: ");

                        Customer customer = CustomerFactory.createCustomer(id, name, phone, loyaltyPoints, false);
                        customerService.addCustomer(customer);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Customer added successfully").reset());
                    }
                    case "3" -> {
                        int id = readInt("Enter Customer ID: ");
                        int amount = readPositiveInt("Enter Amount to Add: ");
                        
                        customerService.addLoyaltyPoints(id, amount);
                        System.out.println(ansi().fgGreen().a(amount + "loyalty points added successfully.").reset());
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());

            }
        }
    }
}
