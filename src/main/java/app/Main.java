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

    /**
     * Constructs the Main application component.
     * 
     * @param context the Spring {@link ApplicationContext} used to retrieve 
     * managed beans like the entry-point menu.
     */
    public Main(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Standard main method to start the Spring Application.
     * 
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * <pre>
     * [UI] <---> [Services] <---> [Repository] <---> [Database]
     * </pre>
     * 
     * Overrides the {@link CommandLineRunner#run} method.
     * 
     * <p>
     * Once the Spring context is fully initialized, this method retrieves the 
     * {@link StoreMenu} bean and triggers the application's primary loop.
     * </p>
     * 
     * @param args command-line arguments.
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

/**
 * [ + ] Spring Boot
 * [ + ] Immutables
 * [ + ] Flyway
 * [ + ] JOOQ
 */