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
    /**
     * drop and recreate schema public ->
     * DROP SCHEMA public CASCADE;
     * CREATE SCHEMA public;
     * 
     * flyway migrate ->
     * mvn clean flyway:migrate
     * 
     * jooq code generation ->
     * mvn clean jooq-codegen:generate
     */
    private final ApplicationContext context;

    public Main(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        /**
         * database ->
         * repository (jooq) | used for SQL queries -> 
         * services | logic for console input and classes methods ->
         * ui | output to console data and get inputs from user
         */

        Menu mainMenu = context.getBean(StoreMenu.class);
        mainMenu.run();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}

/*
[ + ] Spring Boot
[ + ] Immutables
[ + ] Flyway
[ + ] JOOQ
*/