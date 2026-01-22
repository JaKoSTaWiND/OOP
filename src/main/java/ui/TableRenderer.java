package ui;

import java.util.List;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import exceptions.EmptyDataException;
import models.customerModels.Customer;
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
    at.addRow("ID", "TYPE", "NAME", "UNIT PRICE", "QUANTITY", "SPECIFIC DETAILS", "TOTAL", "CATEGORY");
    at.addRule();

    for (Product p : products) {
        at.addRow(
            p.productId(),
            p.getClass().getSimpleName(),
            p.name(),
            p.unitPrice() + " USD",
            p.quantity(),
            p.getSpecificDetails(),
            p.getTotalPrice() + " USD",
            p.category()
        );
        at.addRule();
    }

    at.getRenderer().setCWC(new CWC_FixedWidth()
        .add(5) // ID
        .add(12) // TYPE
        .add(20) // NAME
        .add(12) // UNIT PRICE
        .add(10) // QUANTITY
        .add(30) // SPECIFIC DETAILS
        .add(12) // TOTAL
        .add(15) // CATEGORY
    );

    at.setTextAlignment(TextAlignment.CENTER);
    System.out.println(at.render());
    }

    // --- EMPLOYEE TABLE ---
    public static void printEmployeeTable(List<? extends Employee> employees) throws EmptyDataException {
        if (employees == null || employees.isEmpty()) {
            throw new EmptyDataException("No employees found.");
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "FULL NAME", "POSITION", "HOURLY RATE", "IS FULL TIME", "GET STARTED AT");
        at.addRule();

        for (Employee e : employees) {
            at.addRow(
                e.employeeId(),
                e.fullName(), 
                e.position(), 
                e.hourlyRate() + " USD", 
                e.isFullTime(), 
                e.startedAt());
            at.addRule();
        }

        at.getRenderer().setCWC(new CWC_FixedWidth()
        .add(5) // ID
        .add(20) // FULL NAME
        .add(20) // POSITION
        .add(12) // HOURLY RATE
        .add(15) // IS FULL TIME
        .add(15) // GET STARTED AT
        );

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }

    // --- CUSTOMER TABLE ---
    public static void printCustomerTable(List<? extends Customer> customers) throws EmptyDataException {
        if (customers == null || customers.isEmpty()) {
            throw new EmptyDataException("No customers found.");
        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("ID", "FULL NAME", "PHONE", "LOYLATY POINTS", "IS VIP");
        at.addRule();

        for (Customer c : customers) {
            at.addRow(
                c.customerId(),
                c.fullName(), 
                c.phone(), 
                c.loyaltyPoints(), 
                c.isVip());
            at.addRule();
        }

        at.getRenderer().setCWC(new CWC_FixedWidth()
        .add(5) // ID
        .add(20) // FULL NAME
        .add(20) // PHONE
        .add(25) // LOYALTY POINTS
        .add(15) // IS VIP
        );

        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }
}