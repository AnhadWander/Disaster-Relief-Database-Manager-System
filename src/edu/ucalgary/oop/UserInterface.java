package edu.ucalgary.oop;

/**
 * UserInterface defines the methods an interface class (CLI or GUI) must implement.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public interface UserInterface {

    /**
     * Initializes and starts the user interface.
     */
    void startApplication();

    /**
     * Displays the main menu options.
     */
    void displayMainMenu();

    /**
     * Processes user input at the top-level menu or sub-menus.
     */
    void handleUserInput();

    /**
     * Prompts for victim data, creating a Person row.
     */
    void promptForVictimData();

    /**
     * Prompts for inquiry data.
     */
    void promptForInquiryData();

    /**
     * Manages location-related tasks.
     */
    void manageLocations();

    /**
     * Manages item/supply tasks.
     */
    void manageItems();

    /**
     * Exits the application gracefully.
     */
    void exitApplication();

    /**
     * Checks if the interface is still running.
     *
     * @return true if running, false otherwise
     */
    boolean isRunning();
}
