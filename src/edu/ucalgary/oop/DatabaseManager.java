package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * DatabaseManager handles the actual PostgreSQL connection
 * and basic insert queries for the Person table.
 * Additional logic resides in domain classes.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private Connection connection;
    private boolean connected = false;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ensf380project";
    private static final String USER   = "oop";
    private static final String PASS   = "ucalgary";

    /**
     * Private constructor to enforce singleton usage.
     */
    private DatabaseManager() {
    }

    /**
     * Retrieves the singleton instance of DatabaseManager.
     *
     * @return the single DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Connects to the PostgreSQL database using preset credentials.
     *
     * @throws SQLException if connection fails
     */
    public void connect() throws SQLException {
        if (!connected) {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connected = true;
        }
    }

    /**
     * Checks if the manager is connected.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Gets the underlying JDBC Connection.
     *
     * @return the Connection, or null if not connected
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Disconnects from the database if connected.
     *
     * @throws SQLException if closing fails
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connected = false;
        }
    }

    /**
     * Resets the singleton instance (for testing).
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Inserts a new Person row. Provided for backward compatibility
     * but typically replaced by Person.createPerson(...).
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @param dob       date of birth (yyyy-mm-dd) or null
     * @param gender    the gender string or null
     * @param phone     the phone or null
     * @throws SQLException if insert fails
     */
    public void insertPerson(String firstName, String lastName, String dob, String gender, String phone) throws SQLException {
        if (!connected) {
            throw new IllegalStateException("Cannot insert person: DB not connected");
        }
        String sql = "INSERT INTO person (first_name, last_name, date_of_birth, gender, phone_number) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            if (dob == null) {
                pstmt.setNull(3, java.sql.Types.DATE);
            } else {
                pstmt.setDate(3, java.sql.Date.valueOf(dob));
            }
            pstmt.setString(4, gender);
            pstmt.setString(5, phone);
            pstmt.executeUpdate();
        }
    }

    /**
     * Sets the family group for a person.
     *
     * @param personId the person's ID
     * @param groupId  the family group ID
     * @throws SQLException if update fails
     */
    public void setFamilyGroup(int personId, int groupId) throws SQLException {
        if (!connected) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "UPDATE person SET family_group=? WHERE person_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, personId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Adds a new medical record row.
     *
     * @param personId the person ID
     * @param locationId the location ID
     * @param dateOfTreatment date of treatment
     * @param details details of treatment
     * @throws SQLException if insert fails
     */
    public void addMedicalRecord(int personId, int locationId, String dateOfTreatment, String details) throws SQLException {
        if (!connected) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "INSERT INTO medicalrecord (location_id, person_id, date_of_treatment, treatment_details) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, locationId);
            pstmt.setInt(2, personId);
            if (dateOfTreatment == null || dateOfTreatment.isEmpty()) {
                pstmt.setTimestamp(3, null);
            } else {
                pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(dateOfTreatment + " 00:00:00"));
            }
            pstmt.setString(4, details);
            pstmt.executeUpdate();
        }
    }

    /**
     * Simple method that selects from 'person' as a demonstration.
     *
     * @throws SQLException if query fails
     */
    public void loadData() throws SQLException {
        if (!connected) {
            throw new IllegalStateException("Cannot load data: DB not connected");
        }
        String sql = "SELECT person_id, first_name, last_name, phone_number FROM person";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("People in DB:");
            while (rs.next()) {
                int pid = rs.getInt("person_id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String phone = rs.getString("phone_number");
                System.out.printf(" Person %d => %s %s, phone=%s\n", pid, fName, lName, phone);
            }
        }
    }
}
