package ui.menus.customerMenu;

import java.util.Optional;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

import exceptions.InvalidInputException;
import interfaces.customer.ICustomerService;
import models.customerModels.Customer;
import ui.menus.BaseMenu;

public class AbstractCustomerMenu extends BaseMenu {
    protected final ICustomerService customerService;

    public AbstractCustomerMenu(Scanner scanner, ICustomerService customerService) {
        super(scanner);
        this.customerService = customerService;
    }

    /**
     * Provides a standardized workflow for onboarding new customers into the loyalty system.
     * 
     * <p>
     * This template method manages the UI sequence for customer registration, capturing 
     * essential identity and contact data such as full name and phone number. By default, 
     * new customers are initialized with a non-VIP status. The actual instantiation 
     * logic is delegated to the {@link CustomerCreator} functional interface to maintain 
     * decoupling between the UI prompts and the model's factory logic.
     * 
     * </p>
     * @param creator a functional callback that handles the specific customer instantiation.
     * @see CustomerCreator
     */
    protected void handleAddCustomer(CustomerCreator creator) throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("--- ADD NEW CUSTOMER ---").reset());

        String fullName = readString("Enter Full Name: ");
        String phone = readString("Enter Phone: ");
        int loyaltyPoints = readPositiveInt("Enter Loyalty Points: ");
        boolean isVip = false;

        Customer customer = creator.createCustomer(fullName, phone, loyaltyPoints, isVip);

        customerService.addCustomer(customer);
        System.out.println(ansi().fgGreen().a("Customer added successfully!").reset());
    }

    /**
     * Functional interface for customer instantiation.
     *
     *  <p>
     * Used by {@link #handleAddCustomer(CustomerCreator)} to bridge the gap between 
     * the menu's data collection and the {@link factories.CustomerFactory}. This 
     * abstraction allows the menu to remain agnostic of the specific model implementation details.
     * </p>
     */
    @FunctionalInterface
    public interface CustomerCreator {
        /**
         * Creates a concrete Customer instance.
         *
         * @param fullName      the customer's legal or display name.
         * @param phone         the validated contact phone number.
         * @param loyaltyPoints the initial balance of reward points.
         * @param isVip         flag indicating premium membership status.
         * @return a new {@link Customer} object.
         */
        Customer createCustomer(String fullName, String phone, int loyaltyPoints, boolean isVip);
    }

    /**
     * Executes a standardized customer removal flow.
     * 
     * <p>
     * Provides a guided interface for deleting customer records via unique ID or 
     * registered name. The method ensures that a deletion attempt only proceeds 
     * if the customer is successfully located within the persistence layer, 
     * offering immediate feedback on the operation's outcome.
     * </p>
     */
    protected void handleDeleteCustomer() throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("\n--- DELETE CUSTOMER ---").reset());
        System.out.println("""
        1. By ID
        2. By Name
        """);

        String choice = scanner.nextLine();

        int idToDelete = -1;

        if (choice.equals("1")) {
            int id = readInt("Enter ID to delete: ");
            idToDelete = customerService.findById(id)
                    .map(Customer::customerId)
                    .orElse(-1);
        } else if (choice.equals("2")) {
            String name = readString("Enter Name to delete: ");
            idToDelete = customerService.findByName(name)
                    .map(Customer::customerId)
                    .orElse(-1);
        }

        if (idToDelete != -1) {
            customerService.deleteCustomer(idToDelete);
            System.out.println(ansi().fgGreen().a("Customer deleted successfully").reset());
        } else {
            System.out.println(ansi().fgRed().a("Customer not found in this category.").reset());
        }
    }

    /**
     * Facilitates the retrieval of a customer record specifically for modification tasks.
     * 
     * <p>
     * This method acts as a search utility that abstracts the complexity of 
     * finding a customer before an update. It offers multiple search vectors 
     * (ID or Name) and returns an {@link Optional} to safely handle cases 
     * where the targeted customer does not exist in the system.
     * 
     * </p>
     * @return an {@link Optional} containing the found {@link Customer}, or empty if no match occurs.
     */
    protected Optional<Customer> findCustomerForUpdate() throws InvalidInputException {
        System.out.println("""
        Find by: 
        1. ID
        2. Name
        """);
        String choice = scanner.nextLine();

        return (choice.equals("1")) 
            ? customerService.findById(readInt("Enter ID: "))
            : customerService.findByName(readString("Enter Name: "));
    }
}
