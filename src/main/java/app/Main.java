package app;

import java.util.Scanner;

import interfaces.Menu;
import services.productServices.FreshProductService;
import services.productServices.FrozenProductService;
import services.productServices.SimpleProductService;
import storage.DataStorage;
import ui.menus.StoreMenu;
import ui.menus.productMenus.FreshProductMenu;
import ui.menus.productMenus.FrozenProductMenu;
import ui.menus.productMenus.SimpleProductMenu;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStorage storage = new DataStorage();

        // --- SERVICES ---
        SimpleProductService simpleService = new SimpleProductService(storage);
        FreshProductService freshService = new FreshProductService(storage);
        FrozenProductService frozenService = new FrozenProductService(storage);

        // --- MENUS ( !!! must initialize from bottom to top by menu hierarchy !!!) ---
        Menu freshProductMenu = new FreshProductMenu(freshService, scanner);
        Menu frozenProductMenu = new FrozenProductMenu(frozenService, scanner);
        

        Menu simpleProductMenu = new SimpleProductMenu(simpleService, freshProductMenu, frozenProductMenu, scanner);
        // --- LAUNCH ---
        Menu mainMenu = new StoreMenu(simpleProductMenu, scanner);
        mainMenu.run();
    }
}