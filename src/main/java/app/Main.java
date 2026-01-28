package app;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import interfaces.Menu;
import ui.menus.StoreMenu;

@SpringBootApplication(scanBasePackages = {"app", "services", "storage", "repositories","ui", "interfaces"})
public class Main implements CommandLineRunner {
    private final ApplicationContext context;

    public Main(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Menu mainMenu = context.getBean(StoreMenu.class);
        mainMenu.run();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}

/*
    * [ + ] Spring Boot
    * [ + ] Immutables
    * [ ] Flyway
    * [ ] JOOQ

*/