package ui;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import exceptions.EmptyDataException;
import models.Customer;
import models.employeeModels.Employee;
import models.productModels.Product;

public class TableRenderer {

    // --- PRODUCT TABLE ---
    public static void printProductTable(List<? extends Product> products) throws EmptyDataException {
    if (products == null || products.isEmpty()) {
        throw new EmptyDataException("No products found.");
    }

    AsciiTable at = new AsciiTable();
    at.addRule();
    at.addRow("ID", "TYPE", "NAME", "UNIT PRICE", "WEIGHT", "TEMP", "TOTAL", "CATEGORY");
    at.addRule();

    for (Product p : products) {
        at.addRow(
            p.getId(),
            p.getClass().getSimpleName(),
            p.getName(),
            p.getUnitPrice() + " USD",
            p.getWeight(),
            p.getTemp(),
            p.getTotalPrice() + " USD",
            p.getCategory()
        );
        at.addRule();
    }

    at.getRenderer().setCWC(new CWC_FixedWidth()
        .add(5).add(12).add(20).add(12).add(10).add(10).add(12).add(15)
    );

    at.setTextAlignment(TextAlignment.CENTER);
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

    // --- CUSTOMER TABLE ---
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