package ui.menus.employeeMenus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.employeeServices.ManagerEmployeeService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class ManagerEmployeeMenu extends BaseMenu implements Menu {
    private final ManagerEmployeeService employeeManagerService;


    public ManagerEmployeeMenu(ManagerEmployeeService employeeManagerService, Scanner scanner) {
        super(scanner);
        this.employeeManagerService = employeeManagerService;
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
                    case "1" -> TableRenderer.printEmployeeTable(employeeManagerService.getAllManagers());
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal hourlyRate = readBigDecimal("Enter hourly rate: ");
                        LocalDate startDate = readLocalDate("Enter start date");
                        int teamSize = readInt("Enter team size: ");
                        employeeManagerService.addManager(id, name, hourlyRate, startDate, teamSize);
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
