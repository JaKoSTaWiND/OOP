package app;

import java.util.Scanner;

import interfaces.ICustomerService;
import interfaces.IEmployeeService;
import interfaces.IProductService;
import interfaces.Menu;
import services.CustomerService;
import services.EmployeeService;
import services.ProductService;
import storage.DataStorage;
import ui.menus.StoreMenu;
import ui.menus.customerMenu.SimpleCustomerMenu;
import ui.menus.employeeMenus.CashierEmployeeMenu;
import ui.menus.employeeMenus.ManagerEmployeeMenu;
import ui.menus.employeeMenus.SimpleEmployeeMenu;
import ui.menus.productMenus.FreshProductMenu;
import ui.menus.productMenus.FrozenProductMenu;
import ui.menus.productMenus.SimpleProductMenu;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStorage storage = new DataStorage();

        // --- SERVICES ---
        IProductService productService = new ProductService(storage); // Product 
        IEmployeeService employeeService = new EmployeeService(storage); // Employee
        ICustomerService customerService = new CustomerService(storage); // Customer


        // --- MENUS ( !!! must initialize from bottom to top by menu hierarchy !!!) ---
        Menu freshProductMenu = new FreshProductMenu(productService, scanner); // Fresh Products
        Menu frozenProductMenu = new FrozenProductMenu(productService, scanner); // Frozen Products

        Menu managerEmployeeMenu = new ManagerEmployeeMenu(employeeService, scanner); // Managers
        Menu cashierEmployeeMenu = new CashierEmployeeMenu(employeeService, scanner); // Cashiers


        Menu simpleProductMenu = new SimpleProductMenu(productService, freshProductMenu, frozenProductMenu, scanner); // Products
        Menu simpleEmployeeMenu = new SimpleEmployeeMenu(employeeService, managerEmployeeMenu, cashierEmployeeMenu,scanner); // Employees
        Menu simpleCustomerMenu = new SimpleCustomerMenu(customerService, scanner); // Customers

        // --- LAUNCH ---
        Menu mainMenu = new StoreMenu(simpleProductMenu, simpleEmployeeMenu, simpleCustomerMenu,scanner);
        mainMenu.run();
    }
}