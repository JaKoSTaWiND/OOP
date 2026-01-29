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

    /**
     * <h2>Project Data Flow & Architecture</h2>
     * <p>
     * The system follows a <b>Layered Architecture</b> pattern, ensuring a clean 
     * separation of concerns between user interaction and data persistence:
     * </p>
     * <ol>
     * <li>
     * <b>Database Layer (PostgreSQL)</b>: 
     * The primary storage containing product tables, types, and constraints.
     * </li>
     * <li>
     * <b>Data Access Layer (jOOQ Repository)</b>: 
     * Handles raw SQL execution through type-safe DSL queries. It is responsible 
     * for mapping database {@code Records} into the domain's objects.
     * </li>
     * <li>
     * <b>Service Layer (Business Logic)</b>: 
     * Acts as a mediator between the UI and Repository. This layer performs 
     * calculations (VAT, Discounts), validation, and manages transactions.
     * </li>
     * <li>
     * <b>Presentation Layer (Console UI)</b>: 
     * The entry point for the user. It manages terminal output, processes 
     * {@link java.util.Scanner} inputs, and routes user commands to the appropriate services.
     * </li>
     * </ol>
     *
     * <pre>
     * [UI] <---> [Services] <---> [Repository] <---> [Database]
     * </pre>
     */
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
[ + ] Spring Boot
[ + ] Immutables
[ + ] Flyway
[ + ] JOOQ
*/