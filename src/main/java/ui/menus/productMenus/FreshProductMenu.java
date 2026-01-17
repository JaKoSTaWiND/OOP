package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.productServices.FreshProductService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class FreshProductMenu extends BaseMenu implements Menu {
    private final FreshProductService freshProductService;


    public FreshProductMenu(FreshProductService freshProductService, Scanner scanner) {
        super(scanner);
        this.freshProductService = freshProductService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FRESH PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Fresh Products
            2. Add New  Fresh Product

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
                    case "1" -> {
                        System.out.println(ansi().reset());
                        TableRenderer.printProductTable(freshProductService.getAllFreshProducts());
                    }
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal price = readBigDecimal("Enter price per kg: ");
                        double weight = readDouble("Enter weight: ");
                        String category = readString("Enter category: ");

                        freshProductService.addFreshProduct(id, name, price, category, weight);
                    }
                    case "0" -> back = true;
                }
            }

            catch (EmptyDataException e) {
                System.out.println(ansi().fgRed().a("Error: " + e.getMessage()).reset());
            }
            catch (InvalidInputException e) {
                System.out.println(ansi().fgRed().a("Invalid input: " + e.getMessage()).reset());
            }
        }
    }
}