package edu.ucalgary.oop;

import java.util.Scanner;

/**
 * Main class is the entry point to the application.
 * Prompts user for language, sets up CLI, and runs the loop.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Main {
    /**
     * Main method to start the program.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        LanguageManager languageManager = new LanguageManager();

        Scanner console = new Scanner(System.in);
        System.out.println("Select a language code to use:");
        System.out.println("1) English (en-CA)");
        System.out.println("2) French (fr-CA)");
        System.out.print("Enter your choice: ");
        String choice = console.nextLine().trim();

        String localeCode;
        switch (choice) {
            case "1":
                localeCode = "en-CA";
                break;
            case "2":
                localeCode = "fr-CA";
                break;
            default:
                localeCode = choice;
                break;
        }
        languageManager.loadLocale(localeCode);

        CommandLineInterface cli = CommandLineInterface.getInstance();
        cli.setLanguageManager(languageManager);

        cli.startApplication();
        while (cli.isRunning()) {
            cli.displayMainMenu();
            cli.handleUserInput();
        }

    }
}
