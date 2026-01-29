package ui.menus.employeeMenus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

import exceptions.InvalidInputException;
import interfaces.employee.IEmployeeService;
import models.employeeModels.Employee;
import ui.menus.BaseMenu;

public abstract class AbstractEmployeeMenu extends BaseMenu {
    protected final IEmployeeService employeeService;

    public AbstractEmployeeMenu(Scanner scanner, IEmployeeService employeeService) {
        super(scanner);
        this.employeeService = employeeService;
    }

    /**
     * Provides a standardized workflow for adding new employees to the system.
     * 
     * <p>
     * This template method orchestrates the common UI sequence for personnel 
     * onboarding, such as gathering the employee's full name and hourly rate. 
     * By default, all new employees are initialized with a full-time status. 
     * The final instantiation is delegated to the {@link EmployeeCreator} 
     * functional interface, enabling subclasses to capture role-specific 
     * data (e.g., team size for managers or register numbers for cashiers).
     * </p>
     * 
     * @param roleName the display name of the employee's role (e.g., "MANAGER" or "CASHIER").
     * @param creator  a functional callback that handles the specific employee instantiation.
     * @see EmployeeCreator
     */
    protected void handleAddEmployee(String roleName, EmployeeCreator creator) throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("--- ADD NEW " + roleName + " ---").reset());

        String name = readString("Enter Full Name: ");
        BigDecimal hourlyRate = readBigDecimal("Enter Hourly Rate: ");

        boolean isFullTime = true;

        Employee employee = creator.createEmployee(name, hourlyRate, isFullTime);
        
        employeeService.addEmployee(employee);
        System.out.println(ansi().fgGreen().a(roleName + " added successfully!").reset());
    }

    /**
     * Functional interface for employee instantiation.
     * 
     * <p>
     * Used by {@link #handleAddEmployee(String, EmployeeCreator)} to delegate 
     * the call to specific factory methods after common identity and contract 
     * data have been gathered. This allows for the injection of role-specific 
     * attributes during the creation process.
     * </p>
     */
    @FunctionalInterface
    public interface EmployeeCreator {
        /**
         * Creates a concrete Employee instance.
         *
         * @param name       the gathered full name of the employee.
         * @param rate       the gathered hourly pay rate.
         * @param isFullTime the default employment status flag.
         * @return a concrete implementation of {@link Employee} (e.g., Manager or Cashier).
         */
        Employee createEmployee(String name, BigDecimal rate, boolean isFullTime) throws InvalidInputException;
    }

    /**
     * Executes a generic employee deletion flow with role-specific type safety.
     * 
     * <p>
     * This method provides a standardized UI for removing personnel records by either 
     * their unique ID or full name. To maintain data integrity across different 
     * management menus, it strictly filters the search results using 
     * {@code allowedType.isInstance()}. This prevents accidental cross-role 
     * deletions (e.g., ensuring a {@code ManagerMenu} context cannot be used 
     * to delete a {@code Cashier}).
     * </p>
     * 
     * @param allowedType the specific class of {@link Employee} permitted for 
     * deletion in the current menu context.
     */
    protected void handleDeleteEmployee(Class<? extends Employee> allowedType) throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("\n--- DELETE EMPLOYEE ---").reset());
        System.out.println("""
        1. By ID
        2. By Name
        """);
        String choice = scanner.nextLine();

        int idToDelete = -1;

        if (choice.equals("1")) {
            int id = readInt("Enter ID to delete: ");
            idToDelete = employeeService.findById(id)
                    .filter(allowedType::isInstance)
                    .map(Employee::employeeId)
                    .orElse(-1);
        } else if (choice.equals("2")) {
            String name = readString("Enter Name to delete: ");
            idToDelete = employeeService.findByName(name)
                    .filter(allowedType::isInstance)
                    .map(Employee::employeeId)
                    .orElse(-1);
        }

        if (idToDelete != -1) {
            employeeService.deleteEmployee(idToDelete);
            System.out.println(ansi().fgGreen().a("Employee deleted successfully").reset());
        } else {
            System.out.println(ansi().fgRed().a("Employee not found in this category.").reset());
        }
    }

    /**
     * Generic employee search logic tailored for the update workflow.
     * 
     * <p>
     * This method facilitates the retrieval of an employee by either their ID or Name.
     * It ensures type safety by filtering the result against the {@code allowedType} 
     * and automatically casting it to the required subclass. This ensures that 
     * role-specific fields remain accessible in the subsequent update steps.
     * </p>
     * 
     * @param <T>         the specific subtype of {@link Employee} being searched.
     * @param allowedType the class of {@link Employee} expected (e.g., {@code Manager.class}).
     * @return an {@link Optional} containing the casted employee if found and type matches; 
     * otherwise, an empty {@link Optional}.
     */
    protected <T extends Employee> Optional<T> findEmployeeForUpdate(Class<T> allowedType) throws InvalidInputException {
        System.out.println("""
        Find by: 
        1. ID
        2. Name
        """);
        String choice = scanner.nextLine();

        Optional<Employee> found = (choice.equals("1")) 
            ? employeeService.findById(readInt("Enter ID: ")) 
            : employeeService.findByName(readString("Enter Name: "));

        return found.filter(allowedType::isInstance).map(allowedType::cast);
    }
}
