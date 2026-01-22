package ui.menus.employeeMenus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.EmployeeFactory;
import interfaces.IEmployeeService;
import interfaces.Menu;
import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import ui.TableRenderer;
import ui.menus.BaseMenu;

@Component
public class CashierEmployeeMenu extends BaseMenu implements Menu {
    private final IEmployeeService employeeService;

    public CashierEmployeeMenu(IEmployeeService employeeService, Scanner scanner) {
        super(scanner);
        this.employeeService = employeeService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- CASHIER MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Cashiers
            2. Add New Cashier

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
                    case "1" -> TableRenderer.printEmployeeTable(employeeService.getEmployeesByType(Cashier.class));
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal hourlyRate = readBigDecimal("Enter hourly rate: ");
                        int registerNumbers = readInt("Enter register numbers: ");

                        Employee employee = EmployeeFactory.createCashierEmployee(id, name, hourlyRate, "Cashier", true, LocalDate.now(), registerNumbers);
                        employeeService.addEmployee(employee);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Cashier added successfully").reset());
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
