package ui.menus.employeeMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.EmployeeFactory;
import interfaces.IEmployeeService;
import interfaces.Menu;
import models.employeeModels.Employee;
import models.employeeModels.Manager;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class ManagerEmployeeMenu extends BaseMenu implements Menu {
    private final IEmployeeService employeeService;


    public ManagerEmployeeMenu(IEmployeeService employeeService, Scanner scanner) {
        super(scanner);
        this.employeeService = employeeService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- MANAGERS MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Managers
            2. Add New Manager

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
                    case "1" -> TableRenderer.printEmployeeTable(employeeService.getEmployeesByType(Manager.class));
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal hourlyRate = readBigDecimal("Enter hourly rate: ");
                        int teamSize = readInt("Enter team size: ");

                        Employee employee = EmployeeFactory.createManager(id, name, hourlyRate, teamSize);
                        employeeService.addEmployee(employee);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Manager added successfully").reset());
                        
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
