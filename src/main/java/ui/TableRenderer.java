package ui;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import models.Customer;
import models.Employee;
import models.Product;

public class TableRenderer {

    public static void printProductTable(List<Product> products) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "NAME", "PRICE", "CATEGORY", "IS DISCOUNTED");
        at.addRule();

        for (Product p : products) {
            at.addRow(p.getId(), p.getName(), p.getUnitPrice() + " USD", p.getCategory(), p.isDiscounted());
            at.addRule();
        }

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }

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