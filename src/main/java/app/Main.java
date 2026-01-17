package app;

import storage.DataStorage;
import ui.menus.StoreMenu;

public class Main {
    public static void main(String[] args) {
        // Создаем данные
        DataStorage storage = new DataStorage();
        
        // Создаем меню
        StoreMenu menu = new StoreMenu(storage);
        
        // Запускаем
        menu.run();
    }
}