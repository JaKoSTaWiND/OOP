package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import interfaces.Menu;
import services.productServices.SimpleProductService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class SimpleProductMenu extends BaseMenu implements Menu {
    private final SimpleProductService simpleProductService;
    private final Menu freshMenu;
    private final Menu frozenMenu;

    public SimpleProductMenu(SimpleProductService simpleProductService, Menu freshMenu, Menu frozenMenu, Scanner scanner) {
        super(scanner);
        this.simpleProductService = simpleProductService;
        this.freshMenu = freshMenu;
        this.frozenMenu = frozenMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Products
            2. Add Simple Product
            3. Apply Discount
            4. Calculate Price With VAT (НДС)

            8. Fresh Product Management ->
            9. Frozen Product Management ->
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
                    case "1" -> TableRenderer.printProductTable(simpleProductService.getAllProducts());
                    case "2" -> {
                        int id = readInt("ID: ");
                        String name = readString("Name: ");
                        BigDecimal price = readBigDecimal("Price: ");
                        String cat = readString("Category: ");
                        simpleProductService.addProduct(id, name, price, false, cat);
                    }
                    case "3" -> {
                        int id = readInt("Enter Product ID: ");
                        double discount = readDouble("Enter discount percentage (0.1 for 10%): ");
                        simpleProductService.applyDiscount(id, discount);
                        System.out.println(ansi().fgGreen().a("Discount applied successfully.").reset());
                    }

                    case "8" -> freshMenu.run();
                    case "9" -> frozenMenu.run();
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}