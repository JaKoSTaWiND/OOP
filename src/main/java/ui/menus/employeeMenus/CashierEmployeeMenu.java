package ui.menus.employeeMenus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.employeeServices.CashierEmployeeService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class CashierEmployeeMenu extends BaseMenu implements Menu {
    private final CashierEmployeeService employeeCashierService;

    public CashierEmployeeMenu(CashierEmployeeService employeeCashierService, Scanner scanner) {
        super(scanner);
        this.employeeCashierService = employeeCashierService;
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
                    case "1" -> TableRenderer.printEmployeeTable(employeeCashierService.getAllCashiers());
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal hourlyRate = readBigDecimal("Enter hourly rate: ");
                        LocalDate startDate = readLocalDate("Enter start date");
                        int registerNumbers = readInt("Enter register numbers: ");
                        employeeCashierService.addCashier(id, name, hourlyRate, startDate, registerNumbers);
                    }
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
