package ui.menus.productMenus;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import exceptions.InvalidSettersException;
import interfaces.IProductService;
import interfaces.Menu;
import ui.TableRenderer;
import ui.menus.BaseMenu;

@Component
public class SimpleProductMenu extends BaseMenu implements Menu {
    private final IProductService productService;
    private final Menu freshProductMenu;
    private final Menu frozenProductMenu;

    public SimpleProductMenu(
            IProductService productService,
            @Lazy Menu freshProductMenu,
            @Lazy Menu frozenProductMenu,
            Scanner scanner
        ) {
        super(scanner);
        this.productService = productService;
        this.freshProductMenu = freshProductMenu;
        this.frozenProductMenu = frozenProductMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fgCyan().bold().a("\n--- PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Products
            2. Apply Discount
            3. Calculate Price With VAT (НДС)

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
                    case "1" -> TableRenderer.printProductTable(productService.getAllProducts());
                    case "2" -> {
                        int id = readInt("Enter Product ID: ");
                        double discount = readDouble("Enter discount percentage (0.1 for 10%): ");
                        productService.applyDiscount(id, discount);
                        System.out.println(ansi().fgGreen().a("Discount applied successfully.").reset());
                    }
                    case "3" -> {
                        int id = readInt("Enter Product ID:");
                        double vatRate = readDouble("Enter VAT rate (0.2 for 20%):");
                        productService.calculatePriceWithVAT(id, vatRate);
                        System.out.println(ansi().fgGreen().a("Price with VAT calculated successfully.").reset());
                    }

                    case "8" -> freshProductMenu.run();
                    case "9" -> frozenProductMenu.run();
                    case "0" -> back = true;
                }
            } catch (EmptyDataException | InvalidInputException | InvalidSettersException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}