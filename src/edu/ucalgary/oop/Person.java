package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Abstract Person class providing basic fields (ID, names, phone)
 * and static DB logic to manage Person records.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public abstract class Person {
    private final int personID;
    private String firstName;
    private String lastName;
    private String phone;

    /**
     * Constructs a Person with mandatory fields.
     *
     * @param personID the person ID
     * @param firstName the first name
     * @param lastName the last name
     * @param phone the phone
     * @throws IllegalArgumentException if any name/phone is null or empty
     */
    protected Person(int personID, String firstName, String lastName, String phone) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Gets the person ID.
     *
     * @return the person ID
     */
    public int getPersonID() {
        return personID;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName new first name
     * @throws IllegalArgumentException if empty
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName new last name
     * @throws IllegalArgumentException if empty
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName;
    }

    /**
     * Gets the phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone.
     *
     * @param phone the new phone
     * @throws IllegalArgumentException if empty
     */
    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.phone = phone;
    }

    /**
     * Parses a short gender choice (1,2,3) into a string.
     *
     * @param input the user choice
     * @return a string "Man", "Woman", "Non-binary person", or null
     */
    public static String parseGenderChoice(String input) {
        switch (input) {
            case "1": return "Man";
            case "2": return "Woman";
            case "3": return "Non-binary person";
            default:  return null;
        }
    }

    /**
     * Creates a Person row in DB with the given fields.
     *
     * @param fName first name
     * @param lName last name
     * @param dob   date of birth or null
     * @param gender gender string or null
     * @param phone phone or null
     * @throws SQLException if insert fails
     */
    public static void createPerson(String fName, String lName, String dob, String gender, String phone) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "INSERT INTO person (first_name, last_name, date_of_birth, gender, phone_number) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fName);
            ps.setString(2, lName);
            if (dob == null) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(dob));
            }
            ps.setString(4, gender);
            ps.setString(5, phone);
            ps.executeUpdate();
        }
    }

    /**
     * Lists all persons in ascending order of person_id.
     *
     * @throws SQLException if query fails
     */
    public static void listAllPersonsSafe() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "SELECT person_id, first_name, last_name, phone_number, gender, family_group FROM person ORDER BY person_id ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int pid = rs.getInt("person_id");
                String fn = rs.getString("first_name");
                String ln = rs.getString("last_name");
                String ph = rs.getString("phone_number");
                String ge = rs.getString("gender");
                int fg    = rs.getInt("family_group");
                System.out.printf("ID=%d, Name=%s %s, Phone=%s, Gender=%s, FamilyGroup=%d\n",
                        pid, fn, ln, ph, ge, fg);
            }
        }
    }

    /**
     * Sets the family group for a person in the DB.
     *
     * @param personId the person's ID
     * @param groupId the family group ID
     * @throws SQLException if update fails
     */
    public static void setFamilyGroup(int personId, int groupId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "UPDATE person SET family_group=? WHERE person_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, personId);
            ps.executeUpdate();
        }
    }

    /**
     * Searches a person by ID and prints details, plus med records, supplies, inquiries.
     *
     * @param personId the person's ID
     * @throws SQLException if queries fail
     */
    public static void searchPersonDetails(int personId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sqlP = "SELECT * FROM person WHERE person_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sqlP)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String fn = rs.getString("first_name");
                    String ln = rs.getString("last_name");
                    String ph = rs.getString("phone_number");
                    String ge = rs.getString("gender");
                    int famG  = rs.getInt("family_group");
                    System.out.printf("ID=%d, Name=%s %s, Phone=%s, Gender=%s, FamilyGroup=%d\n",
                            personId, fn, ln, ph, ge, famG);
                } else {
                    System.out.println("No person found with ID=" + personId);
                    return;
                }
            }
        }
        MedicalRecord.listForPerson(personId);
        Item.listSuppliesForPerson(personId);
        Inquiry.listForPerson(personId);
    }

    /**
     * Updates a person's fields. Reorders logic so date parse occurs first if needed.
     *
     * @param personId   the ID
     * @param newFName   new first name or blank
     * @param newLName   new last name or blank
     * @param newDOB     new date of birth or blank
     * @param newGender  new gender or blank
     * @param newPhone   new phone or blank
     * @throws SQLException if update fails
     */
    public static void updatePerson(int personId, String newFName, String newLName, String newDOB, String newGender, String newPhone) throws SQLException {
        java.sql.Date parsedDOB = null;
        if (newDOB != null && !newDOB.isEmpty()) {
            parsedDOB = java.sql.Date.valueOf(newDOB);
        }

        StringBuilder sb = new StringBuilder("UPDATE person SET ");
        boolean needComma = false;

        if (newFName != null && !newFName.isEmpty()) {
            sb.append("first_name=?");
            needComma = true;
        }
        if (newLName != null && !newLName.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("last_name=?");
            needComma = true;
        }
        if (parsedDOB != null) {
            if (needComma) sb.append(", ");
            sb.append("date_of_birth=?");
            needComma = true;
        }
        if (newGender != null && !newGender.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("gender=?");
            needComma = true;
        }
        if (newPhone != null && !newPhone.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("phone_number=?");
            needComma = true;
        }
        if (!needComma) {
            System.out.println("No changes specified.");
            return;
        }
        sb.append(" WHERE person_id=?");
        String sql = sb.toString();

        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (newFName != null && !newFName.isEmpty()) {
                ps.setString(idx++, newFName);
            }
            if (newLName != null && !newLName.isEmpty()) {
                ps.setString(idx++, newLName);
            }
            if (parsedDOB != null) {
                ps.setDate(idx++, parsedDOB);
            }
            if (newGender != null && !newGender.isEmpty()) {
                ps.setString(idx++, newGender);
            }
            if (newPhone != null && !newPhone.isEmpty()) {
                ps.setString(idx++, newPhone);
            }
            ps.setInt(idx, personId);
            ps.executeUpdate();
        }
    }
}
