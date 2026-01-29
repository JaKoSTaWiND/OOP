package ui.menus.employeeMenus;

import java.time.LocalDate;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.EmployeeFactory;
import interfaces.Menu;
import interfaces.employee.IEmployeeService;
import models.employeeModels.BaseCashier;
import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import ui.TableRenderer;

@Component
public class CashierEmployeeMenu extends AbstractEmployeeMenu implements Menu {

    public CashierEmployeeMenu(IEmployeeService employeeService, Scanner scanner) {
        super(scanner, employeeService);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- CASHIER MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Cashiers
            2. Add New Cashier
            3. Delete Casgier
            4. Update Cashier

            0. Back to Main Menu
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
                    case "1" -> TableRenderer.printEmployeeTable(employeeService.getEmployeesByType(Cashier.class)); // list all cashiers

                    case "2" -> handleAddEmployee("CASHIER", (name, rate, fullTime) -> {
                        int regNum = readInt("Enter Register Number: ");
                        return EmployeeFactory.createCashierEmployee(0, name, rate, fullTime, LocalDate.now(), regNum, 0);
                    }); // add new cashier

                    case "3" -> handleDeleteEmployee(Cashier.class); // delete cashier

                    case "4" -> { // update cashier
                        System.out.println(ansi().bold().fgCyan().a("--- UPDATE CASHIER ---").reset());

                        findEmployeeForUpdate(Cashier.class).ifPresentOrElse(cashier -> {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(cashier.fullName()));
                            System.out.println("1. Name | 2. Rate | 3. Register Number | 4. Shift Count");
                            System.out.print("Select field to update > ");
                            String field = scanner.nextLine();

                            try {
                                Employee updated = switch (field) {
                                    case "1" -> BaseCashier.copyOf(cashier)
                                            .withFullName(readString("New Name: "));
                                    
                                    case "2" -> BaseCashier.copyOf(cashier)
                                            .withHourlyRate(readBigDecimal("New Hourly Rate: "));
                                    
                                    case "3" -> BaseCashier.copyOf(cashier)
                                            .withRegisterNumber(readInt("New Register Number: "));
                                    
                                    case "4" -> BaseCashier.copyOf(cashier)
                                            .withShiftCount(readInt("New Shift Count: "));
                                    
                                    default -> cashier;
                                };

                                if (updated != cashier) {
                                    employeeService.updateEmployee(updated);
                                    System.out.println(ansi().fgGreen().a("Cashier updated successfully!").reset());
                                }

                            } catch (InvalidInputException e) {
                                System.out.println(ansi().fgRed().a("Update failed: " + e.getMessage()).reset());
                            }
                        }, () -> System.out.println(ansi().fgRed().a("Cashier not found!").reset()));
                    }

                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
