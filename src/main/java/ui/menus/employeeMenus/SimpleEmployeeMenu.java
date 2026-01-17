package ui.menus.employeeMenus;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import interfaces.Menu;
import services.employeeServices.SimpleEmployeeService;
import ui.TableRenderer;
import ui.menus.BaseMenu;


public class SimpleEmployeeMenu  extends BaseMenu implements Menu {
    private final SimpleEmployeeService simpleEmployeeService;
    private final Menu managerEmployeeMenu;
    private final Menu cashierEmployeeMenu;

    public SimpleEmployeeMenu(SimpleEmployeeService simpleEmployeeService, Menu managerEmployeeMenu, Menu cashierEmployeeMenu, Scanner scanner) {
        super(scanner);
        this.simpleEmployeeService = simpleEmployeeService;
        this.managerEmployeeMenu = managerEmployeeMenu;
        this.cashierEmployeeMenu = cashierEmployeeMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- EMPLOYEE MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Employees

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
                    case "1" -> TableRenderer.printEmployeeTable(simpleEmployeeService.getAllEmployees());
                    case "8" -> managerEmployeeMenu.run();
                    case "9" -> cashierEmployeeMenu.run();
                    case "0" -> back = true;
                }
            } catch (EmptyDataException e) {
                System.out.println(ansi().fgRed().a(e.getMessage()).reset());
            }
        }
    }
}

