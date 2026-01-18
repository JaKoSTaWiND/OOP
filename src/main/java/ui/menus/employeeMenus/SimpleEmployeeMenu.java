package ui.menus.employeeMenus;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.IEmployeeService;
import interfaces.Menu;
import ui.TableRenderer;
import ui.menus.BaseMenu;


public class SimpleEmployeeMenu  extends BaseMenu implements Menu {
    private final IEmployeeService employeeService;
    private final Menu managerEmployeeMenu;
    private final Menu cashierEmployeeMenu;

    public SimpleEmployeeMenu(IEmployeeService employeeService, Menu managerEmployeeMenu, Menu cashierEmployeeMenu, Scanner scanner) {
        super(scanner);
        this.employeeService = employeeService;
        this.managerEmployeeMenu = managerEmployeeMenu;
        this.cashierEmployeeMenu = cashierEmployeeMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- EMPLOYEES MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Employees
            2. Calculate Payroll

            8. Managers ->
            9. Cashiers ->
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
                    case "1" -> TableRenderer.printEmployeeTable(employeeService.getAllEmployees());
                    case "2" -> {
                        int id = readInt("Enter Employee ID: ");
                        int hours = readInt("Enter Worked Hours: ");
                        double bonus = readDouble("Enter Bonus Amount: "); // Not include bonus for teamSize, just a bonus above all calculations
                        System.out.println(ansi().fgGreen().a("Total Payroll: " + employeeService.calculatePayroll(id, hours, bonus)).reset());
                    }
                    case "8" -> managerEmployeeMenu.run();
                    case "9" -> cashierEmployeeMenu.run();
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}

