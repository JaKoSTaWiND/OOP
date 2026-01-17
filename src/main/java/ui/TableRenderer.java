package ui;

import java.util.List;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import exceptions.EmptyDataException;
import models.Customer;
import models.Employee;
import models.FreshProduct;
import models.FrozenProduct;
import models.Product;

public class TableRenderer {

    // --- PRODUCT TABLE ---
    public static void printProductTable(List<Product> products) throws EmptyDataException {
        if (products == null || products.isEmpty()) {
            throw new EmptyDataException("Product list is empty.");
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "TYPE", "NAME", "PRICE", "CATEGORY", "IS DISCOUNTED");
        at.addRule();

        for (Product p : products) {
            String type = p.getClass().getSimpleName();

            at.addRow(p.getId(), type, p.getName(), p.getUnitPrice() + " USD", p.getCategory(), p.isDiscounted());
            at.addRule();
        }

        at.getRenderer().setCWC(new CWC_FixedWidth()
        .add(5)   // ID
        .add(15)  // TYPE
        .add(25)  // NAME
        .add(20)  // PRICE
        .add(20)  // CATEGORY
        .add(10)  // IS DISCOUNTED
        );

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- PRODUCTS ---").reset());
        System.out.println(at.render());
    }

    // --- FRESH PRODUCT TABLE ---
    public static void printFreshProductTable(List<FreshProduct> products) throws EmptyDataException {
        if (products == null || products.isEmpty()) {
            throw new EmptyDataException("FreshProduct list is empty.");
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME", "PRICE/kg", "WEIGHT", "TOTAL", "IS BULK","CATEGORY");
        at.addRule();

        for (FreshProduct p : products) {
            at.addRow(
                p.getId(), 
                p.getName(), 
                p.getUnitPrice() + " USD", 
                p.getWeight() + " kg",
                p.calculateTotalWeightPrice() + " USD",
                p.isBulk(),
                p.getCategory()
            );
            at.addRule();
        }

        at.getRenderer().setCWC(new CWC_FixedWidth()
            .add(5)   // ID
            .add(25)  // NAME
            .add(15)  // PRICE/kg
            .add(10)  // WEIGHT
            .add(15)  // TOTAL
            .add(10)  // IS BULK
            .add(20)  // CATEGORY
        );

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FRESH PRODUCTS ---").reset());
        System.out.println(at.render());
    }

    // --- FROZEN PRODUCT TABLE ---
    public static void printFrozenProductTable(List<FrozenProduct> products) throws EmptyDataException {
        if (products == null || products.isEmpty()) {
            throw new EmptyDataException("FrozenProduct list is empty.");
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME", "PRICE", "STORAGE TEMP", "IS DEEP FREEZE", "CATEGORY");
        at.addRule();

        for (FrozenProduct p : products) {
            at.addRow(
                p.getId(), 
                p.getName(), 
                p.getUnitPrice() + " USD",
                p.getStorageTemp() + " °C",
                p.isDeepFreeze(),
                p.getCategory()
            );
            at.addRule();
        }

        at.getRenderer().setCWC(new CWC_FixedWidth()
            .add(5)   // ID
            .add(25)  // NAME
            .add(15)  // PRICE
            .add(15)  // STORAGE TEMP
            .add(10)  // IS DEEP FREEZE
            .add(20)  // CATEGORY
        );

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FROZEN PRODUCTS ---").reset());
        System.out.println(at.render());
    }

    // --- EMPLOYEE TABLE ---
    public static void printEmployeeTable(List<Employee> employees) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "FULL NAME", "POSITION", "HOURLY RATE", "IS FULL TIME", "GET STARTED AT");
        at.addRule();

        for (Employee e : employees) {
            at.addRow(e.getId(), e.getFullName(), e.getPosition(), e.getHourlyRate() + " USD", e.getIsFullTime(), e.getStartedAt());
            at.addRule();
        }

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }

    public static void printCustomerTable(List<Customer> customers) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "FULL NAME", "PHONE", "LOYLATY POINTS", "IS VIP");
        at.addRule();

        for (Customer c : customers) {
            at.addRow(c.getId(), c.getFullName(), c.getPhone(), c.getLoyaltyPoints(), c.isVip());
            at.addRule();
        }

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }
}