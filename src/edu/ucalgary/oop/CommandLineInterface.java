package edu.ucalgary.oop;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * CommandLineInterface is a singleton class that orchestrates the user menu
 * for managing victims, inquiries, items (supplies), and locations.
 *
 * <p>
 * Features:
 * - Manage Victims: add, list, set family group, add medical record, search by ID, edit victim, edit medical record
 * - Manage Inquiries: create inquiry, list inquiries, edit existing inquiry
 * - Manage Items (Supplies): add new supply, allocate supply to person or location, list, edit supply
 * - Manage Locations: reassign occupant, show occupant mappings, search location by ID, edit location
 * - Removes expired water allocations at startup
 * </p>
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class CommandLineInterface implements UserInterface {

    private static CommandLineInterface instance = null;

    private boolean running = false;
    private final ErrorLogger errorLogger;
    private final Scanner scanner;
    private LanguageManager languageManager;

    /**
     * Private constructor for singleton usage.
     * @param logger the error logger
     */
    private CommandLineInterface(ErrorLogger logger) {
        this.errorLogger = logger;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Retrieves the singleton instance of CommandLineInterface.
     * @return the singleton instance
     */
    public static CommandLineInterface getInstance() {
        if (instance == null) {
            instance = new CommandLineInterface(new ErrorLogger());
        }
        return instance;
    }

    /**
     * Resets the singleton instance, used in testing or re-init.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Sets the LanguageManager for translations.
     * @param lm the LanguageManager
     */
    public void setLanguageManager(LanguageManager lm) {
        this.languageManager = lm;
    }

    /**
     * Starts the application, connecting to DB and removing expired water if any.
     */
    @Override
    public void startApplication() {
        running = true;
        System.out.println(getTranslation("welcome_message"));
        try {
            DatabaseManager.getInstance().connect();
            Water.removeExpiredAllocations();
        } catch (SQLException e) {
            criticalDBError(getTranslation("db_connect_fail"), e);
        }
    }

    /**
     * Displays the main menu.
     */
    @Override
    public void displayMainMenu() {
        System.out.println("\n" + getTranslation("menu_main_title"));
        System.out.println(getTranslation("menu_option_1"));
        System.out.println(getTranslation("menu_option_2"));
        System.out.println(getTranslation("menu_option_3"));
        System.out.println(getTranslation("menu_option_4"));
        System.out.println(getTranslation("menu_option_5"));
        System.out.print(getTranslation("prompt_choice"));
    }

    /**
     * Handles user input at the main menu.
     */
    @Override
    public void handleUserInput() {
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                manageVictimsMenu();
                break;
            case "2":
                manageInquiriesMenu();
                break;
            case "3":
                manageItems();
                break;
            case "4":
                manageLocations();
                break;
            case "5":
                exitApplication();
                break;
            default:
                System.out.println(getTranslation("error_invalid_choice"));
        }
    }


    /**
     * Sub-menu for managing victims:
     * 1) Add Victim
     * 2) List Victims
     * 3) Set Family Group
     * 4) Add Medical Record
     * 5) Search Person by ID
     * 6) Edit Existing Victim
     * 7) Edit Existing Medical Record
     * 8) Return to Main Menu
     */
    private void manageVictimsMenu() {
        while (true) {
            System.out.println("\n" + getTranslation("menu_victims_title"));
            System.out.println(getTranslation("menu_victims_option_1"));
            System.out.println(getTranslation("menu_victims_option_2"));
            System.out.println(getTranslation("menu_victims_option_3"));
            System.out.println(getTranslation("menu_victims_option_4"));
            System.out.println(getTranslation("menu_victims_option_5"));
            System.out.println(getTranslation("menu_victims_option_6"));
            System.out.println(getTranslation("menu_victims_option_7"));
            System.out.println(getTranslation("menu_victims_option_8"));
            System.out.print(getTranslation("prompt_choice"));

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    promptForVictimData();
                    break;
                case "2":
                    try {
                        Person.listAllPersonsSafe();
                    } catch (SQLException e) {
                        System.out.println("Error listing victims: " + e.getMessage());
                        errorLogger.logError("Failed to query victims", e);
                    }
                    break;
                case "3":
                    promptSetFamilyGroup();
                    break;
                case "4":
                    promptAddMedicalRecord();
                    break;
                case "5":
                    searchPersonById();
                    break;
                case "6":
                    promptEditVictim();
                    break;
                case "7":
                    promptEditMedicalRecord();
                    break;
                default:
                    return;
            }
        }
    }

    /**
     * Prompts user for new victim data.
     */
    @Override
    public void promptForVictimData() {
        System.out.println("\n" + getTranslation("prompt_add_victim_title"));
        try {
            System.out.print(getTranslation("prompt_first_name"));
            String fName = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_last_name"));
            String lName = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_birth_date"));
            String dob = scanner.nextLine().trim();
            if (dob.isEmpty()) dob = null;

            System.out.println(getTranslation("prompt_gender_options"));
            String gChoice = scanner.nextLine().trim();
            String genderStr = Person.parseGenderChoice(gChoice);

            System.out.print(getTranslation("prompt_phone"));
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = null;

            Person.createPerson(fName, lName, dob, genderStr, phone);
            System.out.println(getTranslation("victim_insert_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to insert victim", e);
        }
    }

    /**
     * Prompts the user to set a family group for a person.
     */
    private void promptSetFamilyGroup() {
        System.out.println("\nSet Family Group (Relationships)");
        try {
            int pID = readIntOrRetry("prompt_person_id");
            System.out.print("Family group ID: ");
            String fgStr = scanner.nextLine().trim();
            int fg = Integer.parseInt(fgStr);

            Person.setFamilyGroup(pID, fg);
            System.out.println("Family group set successfully for person " + pID);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to set family group", e);
        } catch (NumberFormatException e) {
            System.out.println("Invalid group ID. Must be integer.");
        }
    }

    /**
     * Prompts the user to add a medical record for a person.
     */
    private void promptAddMedicalRecord() {
        System.out.println("\nAdd Medical Record");
        try {
            int pID = readIntOrRetry("prompt_person_id");
            int lID = readIntOrRetry("prompt_loc_id");

            System.out.print("Date of treatment (yyyy-mm-dd or blank=none): ");
            String dateStr = scanner.nextLine().trim();
            if (dateStr.isEmpty()) dateStr = null;

            System.out.print("Treatment details: ");
            String details = scanner.nextLine().trim();
            if (details.isEmpty()) details = null;

            MedicalRecord.insertRecord(pID, lID, dateStr, details);
            System.out.println("Medical record added successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to add medical record", e);
        }
    }

    /**
     * Searches a person by ID and shows details (victim data, supplies, inquiries, med records).
     */
    private void searchPersonById() {
        System.out.println("\n--- Search Person by ID ---");
        try {
            int pID = readIntOrRetry("prompt_person_id");
            Person.searchPersonDetails(pID);
        } catch (SQLException e) {
            System.out.println("SQL Error in searchPersonById: " + e.getMessage());
            errorLogger.logError("Failed to search person by ID", e);
        }
    }

    /**
     * Prompts user to edit a victim's fields.
     */
    private void promptEditVictim() {
        System.out.println("\n" + getTranslation("prompt_edit_victim_title"));
        try {
            int pID = readIntOrRetry("prompt_person_id");

            System.out.print(getTranslation("prompt_new_victim_name"));
            String newFName = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_victim_lastname"));
            String newLName = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_victim_dob"));
            String newDOB   = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_victim_gender"));
            String gChoice  = scanner.nextLine().trim();
            String newGender = Person.parseGenderChoice(gChoice);

            System.out.print(getTranslation("prompt_new_victim_phone"));
            String newPhone = scanner.nextLine().trim();

            Person.updatePerson(pID, newFName, newLName, newDOB, newGender, newPhone);
            System.out.println(getTranslation("victim_update_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error updating victim: " + e.getMessage());
            errorLogger.logError("Failed to update victim", e);
        }
    }

    /**
     * Prompts user to edit an existing medical record.
     */
    private void promptEditMedicalRecord() {
        System.out.println("\n--- Edit Existing Medical Record ---");
        try {
            System.out.print("Enter medical_record_id to edit: ");
            String recStr = scanner.nextLine().trim();
            if (recStr.isEmpty()) {
                System.out.println("No ID provided.");
                return;
            }
            int recID = Integer.parseInt(recStr);

            System.out.print("Enter new location ID (blank=skip): ");
            String newLocID = scanner.nextLine().trim();

            System.out.print("Enter new date (yyyy-mm-dd, blank=skip): ");
            String newDate  = scanner.nextLine().trim();

            System.out.print("Enter new treatment details (blank=skip): ");
            String newDetails = scanner.nextLine().trim();

            MedicalRecord.updateMedicalRecord(recID, newLocID, newDate, newDetails);
            System.out.println("Medical record updated successfully.");

        } catch (SQLException e) {
            System.out.println("SQL Error updating medical record: " + e.getMessage());
            errorLogger.logError("Failed to update medical record", e);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input for record ID.");
        }
    }


    private void manageInquiriesMenu() {
        while (true) {
            System.out.println("\n" + getTranslation("menu_inquiries_title"));
            System.out.println(getTranslation("menu_inquiries_option_1"));
            System.out.println(getTranslation("menu_inquiries_option_2"));
            System.out.println(getTranslation("menu_inquiries_option_3"));
            System.out.println(getTranslation("menu_inquiries_option_4"));
            System.out.print(getTranslation("prompt_choice"));

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    promptForInquiryData();
                    break;
                case "2":
                    listInquiriesFromDB();
                    break;
                case "3":
                    promptEditInquiry();
                    break;
                default:
                    return;
            }
        }
    }

    /**
     * Prompts for new inquiry data.
     */
    @Override
    public void promptForInquiryData() {
        System.out.println("\n" + getTranslation("prompt_inquiry_title"));
        try {
            int inquirerID = readIntOrRetry("prompt_inquirer_id");
            int missingID  = readIntOrRetry("prompt_missing_id");
            int locID      = readIntOrRetry("prompt_loc_id_or_zero");
            Integer locObj = (locID == 0 ? null : locID);

            System.out.print(getTranslation("prompt_inquiry_date"));
            String dateStr = scanner.nextLine().trim();
            if (dateStr.isEmpty()) {
                dateStr = "2025-01-01";
            }

            System.out.print(getTranslation("prompt_inquiry_comments"));
            String comments = scanner.nextLine().trim();
            if (comments.isEmpty()) comments = null;

            Inquiry.createInquiry(inquirerID, missingID, locObj, dateStr, comments);
            System.out.println(getTranslation("inquiry_logged_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to insert inquiry", e);
        }
    }

    private void listInquiriesFromDB() {
        System.out.println("\n" + getTranslation("list_inquiries_header"));
        try {
            Inquiry.listAllInquiriesSafe();
        } catch (SQLException e) {
            System.out.println("SQL Error listing inquiries: " + e.getMessage());
            errorLogger.logError("Failed to query inquiries", e);
        }
    }

    /**
     * Prompts user to edit an existing inquiry.
     */
    private void promptEditInquiry() {
        System.out.println("\n" + getTranslation("prompt_edit_inquiry_title"));
        try {
            System.out.print(getTranslation("prompt_inquiry_id"));
            String inqIDstr = scanner.nextLine().trim();
            if (inqIDstr.isEmpty()) {
                System.out.println("No inquiry ID provided.");
                return;
            }
            int inqID = Integer.parseInt(inqIDstr);

            System.out.print(getTranslation("prompt_new_inquirer_id"));
            String newInqer   = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_missing_id"));
            String newMissing = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_loc_id"));
            String newLocId   = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_inquiry_date"));
            String newDate    = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_inquiry_comments"));
            String newComments= scanner.nextLine().trim();

            Inquiry.updateInquiry(inqID, newInqer, newMissing, newLocId, newDate, newComments);
            System.out.println(getTranslation("inquiry_update_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error updating inquiry: " + e.getMessage());
            errorLogger.logError("Failed to update inquiry", e);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input for inquiry_id.");
        }
    }


    @Override
    public void manageItems() {
        while (true) {
            System.out.println("\n" + getTranslation("menu_items_title"));
            System.out.println(getTranslation("menu_items_option_1"));
            System.out.println(getTranslation("menu_items_option_2"));
            System.out.println(getTranslation("menu_items_option_3"));
            System.out.println(getTranslation("menu_items_option_4"));
            System.out.println(getTranslation("menu_items_option_5"));
            System.out.println(getTranslation("menu_items_option_6"));
            System.out.print(getTranslation("prompt_choice"));

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    promptAddNewSupply();
                    break;
                case "2":
                    allocateSupplyToPerson();
                    break;
                case "3":
                    allocateSupplyToLocation();
                    break;
                case "4":
                    try {
                        Item.listAllSuppliesSafe();
                    } catch (SQLException e) {
                        System.out.println("SQL Error listing supplies: " + e.getMessage());
                        errorLogger.logError("Failed to query supplies", e);
                    }
                    break;
                case "5":
                    promptEditSupply();
                    break;
                default:
                    return;
            }
        }
    }

    /**
     * Prompts user to add a new supply item, selecting from 4 types (personal belonging, blanket, water, cot).
     * If "cot" is chosen, prompt for numeric room and letter+digits grid coordinate, store in "comments".
     */
    private void promptAddNewSupply() {
        System.out.println("\n" + getTranslation("prompt_add_supply_title"));
        String supplyType = promptSupplyType();

        String comments = null;
        if (supplyType.equalsIgnoreCase("cot")) {
            System.out.print(getTranslation("prompt_cot_room"));
            String roomStr = scanner.nextLine().trim();
            while (!roomStr.matches("^\\d+$")) {
                System.out.println(getTranslation("error_invalid_room"));
                System.out.print(getTranslation("prompt_cot_room"));
                roomStr = scanner.nextLine().trim();
            }

            System.out.print(getTranslation("prompt_cot_grid"));
            String gridStr = scanner.nextLine().trim();
            while (!gridStr.matches("^[A-Za-z]\\d+$")) {
                System.out.println(getTranslation("error_invalid_grid"));
                System.out.print(getTranslation("prompt_cot_grid"));
                gridStr = scanner.nextLine().trim();
            }

            comments = "room=" + roomStr + ",grid=" + gridStr;
        } else {
            System.out.print(getTranslation("prompt_supply_comments"));
            String userComm = scanner.nextLine().trim();
            if (!userComm.isEmpty()) {
                comments = userComm;
            }
        }

        try {
            Item.createSupply(supplyType, comments);
            System.out.println(getTranslation("supply_insert_success"));
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to insert supply", e);
        }
    }

    /**
     * Allows user to pick the supply type from 4 options.
     */
    private String promptSupplyType() {
        System.out.println(getTranslation("prompt_supply_type_choice"));
        while (true) {
            System.out.print(getTranslation("prompt_choice"));
            String pick = scanner.nextLine().trim();
            switch (pick) {
                case "1": return "personal belonging";
                case "2": return "blanket";
                case "3": return "water";
                case "4": return "cot";
                default:
                    System.out.println(getTranslation("error_invalid_choice"));
            }
        }
    }

    private void allocateSupplyToPerson() {
        System.out.println("\n" + getTranslation("allocate_supply_person"));
        try {
            int sID = readIntOrRetry("prompt_supply_id");
            int pID = readIntOrRetry("prompt_person_id");

            Item.allocateToPerson(sID, pID);
            System.out.println(getTranslation("allocation_success"));
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to allocate supply to person", e);
        }
    }

    private void allocateSupplyToLocation() {
        System.out.println("\n" + getTranslation("allocate_supply_location"));
        try {
            int sID = readIntOrRetry("prompt_supply_id");
            int lID = readIntOrRetry("prompt_loc_id");

            Item.allocateToLocation(sID, lID);
            System.out.println(getTranslation("allocation_success"));
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to allocate supply to location", e);
        }
    }

    /**
     * Prompts user to edit an existing supply record (type, comments).
     */
    private void promptEditSupply() {
        System.out.println("\n" + getTranslation("prompt_edit_supply_title"));
        try {
            int supplyId = readIntOrRetry("prompt_existing_supply_id");

            System.out.print(getTranslation("prompt_new_supply_type"));
            String newType = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_supply_comments"));
            String newComm = scanner.nextLine().trim();

            Item.updateSupply(supplyId, newType, newComm);
            System.out.println(getTranslation("update_supply_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error updating supply: " + e.getMessage());
            errorLogger.logError("Failed to update supply", e);
        }
    }


    @Override
    public void manageLocations() {
        while (true) {
            System.out.println("\n" + getTranslation("menu_locations_title"));
            System.out.println(getTranslation("menu_locations_option_1"));
            System.out.println(getTranslation("menu_locations_option_2"));
            System.out.println(getTranslation("menu_locations_option_3"));
            System.out.println(getTranslation("menu_locations_option_4"));
            System.out.println(getTranslation("menu_locations_option_5"));
            System.out.print(getTranslation("prompt_choice"));

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    reassignPersonLocation();
                    break;
                case "2":
                    try {
                        Location.listPersonLocationsSafe();
                    } catch (SQLException e) {
                        System.out.println("SQL Error: " + e.getMessage());
                        errorLogger.logError("Failed to query PersonLocation", e);
                    }
                    break;
                case "3":
                    searchLocationById();
                    break;
                case "4":
                    promptEditLocation();
                    break;
                default:
                    return;
            }
        }
    }

    private void reassignPersonLocation() {
        System.out.println("\n" + getTranslation("reassign_person_loc"));
        try {
            int pID  = readIntOrRetry("prompt_person_id");
            int locID= readIntOrRetry("prompt_loc_id");

            Location.reassignPersonToLocation(pID, locID);
            System.out.println(getTranslation("reassign_success"));
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            errorLogger.logError("Failed to reassign location", e);
        }
    }

    private void searchLocationById() {
        System.out.println("\n--- Search Location by ID ---");
        try {
            int locId = readIntOrRetry("prompt_loc_id");
            Location.searchLocationDetailsSafe(locId);
        } catch (SQLException e) {
            System.out.println("SQL Error in searchLocationById: " + e.getMessage());
            errorLogger.logError("Failed to search location by ID", e);
        }
    }

    /**
     * Prompts user to edit an existing location record (name, address).
     */
    private void promptEditLocation() {
        System.out.println("\n" + getTranslation("prompt_edit_location_title"));
        try {
            System.out.print(getTranslation("prompt_location_id_for_edit"));
            String locStr = scanner.nextLine().trim();
            if (locStr.isEmpty()) {
                System.out.println("No location ID provided.");
                return;
            }
            int locId = Integer.parseInt(locStr);

            System.out.print(getTranslation("prompt_new_location_name"));
            String newName = scanner.nextLine().trim();

            System.out.print(getTranslation("prompt_new_location_addr"));
            String newAddr = scanner.nextLine().trim();

            Location.updateLocation(locId, newName, newAddr);
            System.out.println(getTranslation("location_update_success"));

        } catch (SQLException e) {
            System.out.println("SQL Error updating location: " + e.getMessage());
            errorLogger.logError("Failed to update location", e);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric location ID.");
        }
    }



    @Override
    public void exitApplication() {
        running = false;
        System.out.println("\n" + getTranslation("exit_goodbye"));
        try {
            DatabaseManager.getInstance().disconnect();
        } catch (SQLException e) {
            System.out.println("Error disconnecting DB: " + e.getMessage());
            errorLogger.logError("Disconnect DB failure", e);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void criticalDBError(String msg, Exception e) {
        System.out.println("Critical DB error: " + msg + " => " + e.getMessage());
        errorLogger.logError(msg, e);
        running = false;
    }

    /**
     * Reads an integer from user with re-prompt on invalid input.
     * @param promptKey the translation key
     * @return the parsed integer
     */
    private int readIntOrRetry(String promptKey) {
        while (true) {
            System.out.print(getTranslation(promptKey));
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(getTranslation("error_invalid_number"));
            }
        }
    }

    /**
     * Retrieves a string from LanguageManager by key.
     * @param key the translation key
     * @return the translated string or placeholder if missing
     */
    private String getTranslation(String key) {
        if (languageManager == null) {
            return "[No LM: " + key + "]";
        }
        String val = languageManager.getString(key);
        if (val == null) {
            return "[Missing translation: " + key + "]";
        }
        return val;
    }
}
