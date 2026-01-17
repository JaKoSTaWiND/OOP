package app;

import java.util.Scanner;

import interfaces.Menu;
import services.employeeServices.CashierEmployeeService;
import services.employeeServices.ManagerEmployeeService;
import services.employeeServices.SimpleEmployeeService;
import services.productServices.FreshProductService;
import services.productServices.FrozenProductService;
import services.productServices.SimpleProductService;
import storage.DataStorage;
import ui.menus.StoreMenu;
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
        SimpleProductService simpleProductService = new SimpleProductService(storage); // Simple Products
        FreshProductService freshService = new FreshProductService(storage); // Fresh Products
        FrozenProductService frozenService = new FrozenProductService(storage); // Frozen Products

        SimpleEmployeeService simpleEmployeeService = new SimpleEmployeeService(storage); // Employees
        ManagerEmployeeService managerEmployeeService = new ManagerEmployeeService(storage); // Managers
        CashierEmployeeService cashierEmployeeService = new CashierEmployeeService(storage); // Cashiers


        // --- MENUS ( !!! must initialize from bottom to top by menu hierarchy !!!) ---
        Menu freshProductMenu = new FreshProductMenu(freshService, scanner); // Fresh Products
        Menu frozenProductMenu = new FrozenProductMenu(frozenService, scanner); // Frozen Products

        Menu managerEmployeeMenu = new ManagerEmployeeMenu(managerEmployeeService, scanner); // Managers
        Menu cashierEmployeeMenu = new CashierEmployeeMenu(cashierEmployeeService, scanner); // Cashiers
        

        Menu simpleProductMenu = new SimpleProductMenu(simpleProductService, freshProductMenu, frozenProductMenu, scanner); // Products
        Menu simpleEmployeeMenu = new SimpleEmployeeMenu(simpleEmployeeService, managerEmployeeMenu, cashierEmployeeMenu,scanner); // Employees

        // --- LAUNCH ---
        Menu mainMenu = new StoreMenu(simpleProductMenu, simpleEmployeeMenu, scanner);
        mainMenu.run();
    }
}