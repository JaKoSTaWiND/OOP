package ui.menus.customerMenu;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.CustomerFactory;
import interfaces.Menu;
import interfaces.customer.ICustomerService;
import models.customerModels.BaseCustomer;
import models.customerModels.Customer;
import ui.TableRenderer;


@Component
public class SimpleCustomerMenu extends AbstractCustomerMenu implements Menu {

    public SimpleCustomerMenu(ICustomerService customerService, Scanner scanner) {
        super(scanner, customerService);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- CUSTOMERS MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Customers
            2. Add New Customer
            3. Delete Customer
            4. Update Customer


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
                    case "2" -> handleAddCustomer((fullName, phone, loyaltyPoints, isVip) -> 
                        CustomerFactory.createCustomer(0, fullName, phone, loyaltyPoints, isVip)
                    );
                    case "3" -> handleDeleteCustomer();
                    case "4" -> { // update customer
                        System.out.println(ansi().bold().fgCyan().a("--- UPDATE CUSTOMER ---").reset());

                        findCustomerForUpdate().ifPresentOrElse(customer -> {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(customer.fullName()));
                            System.out.println("1. Name | 2. Phone | 3. Loyalty Points");
                            System.out.print("Select field to update > ");
                            String field = scanner.nextLine();

                            try {
                                Customer updated = switch (field) {
                                    case "1" -> BaseCustomer.copyOf(customer)
                                            .withFullName(readString("New Name: "));
                                    
                                    case "2" -> BaseCustomer.copyOf(customer)
                                            .withPhone(readString("New Phone: "));
                                    
                                    case "3" -> BaseCustomer.copyOf(customer)
                                            .withLoyaltyPoints(readPositiveInt("New Loyalty Points: "));
                                    
                                    default -> customer;
                                };

                                if (updated != customer) {
                                    customerService.updateCustomer(updated);
                                    System.out.println(ansi().fgGreen().a("Customer updated successfully!").reset());
                                }

                            } catch (InvalidInputException e) {
                                System.out.println(ansi().fgRed().a("Update failed: " + e.getMessage()).reset());
                            }
                        }, () -> System.out.println(ansi().fgRed().a("Customer not found!").reset()));
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());

            }
        }
    }
}
