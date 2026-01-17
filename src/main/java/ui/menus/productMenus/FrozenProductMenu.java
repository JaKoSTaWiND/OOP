package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.productServices.FrozenProductService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class FrozenProductMenu extends BaseMenu implements Menu {
    private final FrozenProductService frozenProductService;

    public FrozenProductMenu(FrozenProductService frozenProductService, Scanner scanner) {
        super(scanner);
        this.frozenProductService = frozenProductService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FROZEN PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Frozen Products
            2. Add New Frozen Product

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
                        TableRenderer.printProductTable(frozenProductService.getAllFrozenProducts());
                    }
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal price = readBigDecimal("Enter price: ");
                        int storageTemp = readInt("Enter storage temperature (°C): ");
                        String category = readString("Enter category: ");

                        frozenProductService.addFrozenProduct(id, name, price, storageTemp, category);
                    }
                    case "0" -> back = true;
                }
            } 
            catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
