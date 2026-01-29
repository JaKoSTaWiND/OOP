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
import models.employeeModels.BaseManager;
import models.employeeModels.Employee;
import models.employeeModels.Manager;
import ui.TableRenderer;

@Component
public class ManagerEmployeeMenu extends AbstractEmployeeMenu implements Menu {

    public ManagerEmployeeMenu(IEmployeeService employeeService, Scanner scanner) {
        super(scanner, employeeService);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- MANAGERS MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Managers
            2. Add New Manager
            3. Delete Manager
            4. Update Manager

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
                    case "1" -> TableRenderer.printEmployeeTable(employeeService.getEmployeesByType(Manager.class)); // list all managers
                    
                    case "2" -> handleAddEmployee("MANAGER", (name, rate, fullTime) -> {
                        int teamSize = readInt("Enter Team Size: ");
                        return EmployeeFactory.createManagerEmployee(0, name, rate, fullTime, LocalDate.now(), teamSize);
                    }); // add new manager

                    case "3" -> handleDeleteEmployee(Manager.class); // delete manager

                    case "4" -> { // update manager
                        System.out.println(ansi().bold().fgCyan().a("--- UPDATE MANAGER ---").reset());

                        findEmployeeForUpdate(Manager.class).ifPresentOrElse(manager -> {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(manager.fullName()));
                            System.out.println("1. Name | 2. Rate | 3. Team Size");
                            String field = scanner.nextLine();

                            try {
                                Employee updated = switch (field) {
                                    case "1" -> BaseManager.copyOf(manager)
                                            .withFullName(readString("New Name: "));
                                    case "2" -> BaseManager.copyOf(manager)
                                            .withHourlyRate(readBigDecimal("New Houtly Rate: "));
                                    case "3" -> BaseManager.copyOf(manager)
                                            .withTeamSize(readInt("New Team Size: "));
                                    default -> manager;
                                };

                                if (updated != manager) {
                                    employeeService.updateEmployee(updated);
                                    System.out.println(ansi().fgGreen().a("Updated successfully!").reset());
                                }
                            } catch (InvalidInputException e) {
                                System.out.println(ansi().fgRed().a("Update failed: " + e.getMessage()).reset());
                            }
                        }, () -> System.out.println(ansi().fgRed().a("Manager not found!").reset()));
                    }
                    
                    case "0" -> back = true;
                } 
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        } 
    } 
}