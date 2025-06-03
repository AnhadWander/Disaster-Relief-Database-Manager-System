package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * Inquiry represents a record of someone inquiring about a missing person.
 * Also provides static DB logic to create, list, and update inquiries.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Inquiry {
    private final int inquiryID;
    private Person inquirer;
    private Person missingPerson;
    private String dateOfInquiry;
    private String infoProvided;
    private Location lastKnownLocation;

    /**
     * Constructs an Inquiry object in memory.
     *
     * @param inquiryID the ID
     * @param inquirer the inquirer Person
     * @param missingPerson the missing Person
     * @param dateOfInquiry date of inquiry (yyyy-MM-dd)
     * @param infoProvided extra info
     * @param lastKnownLocation the location
     * @throws IllegalArgumentException if date is invalid
     */
    public Inquiry(int inquiryID, Person inquirer, Person missingPerson,
                   String dateOfInquiry, String infoProvided, Location lastKnownLocation) {
        if (inquirer == null) {
            throw new IllegalArgumentException("Inquirer cannot be null");
        }
        if (missingPerson == null) {
            throw new IllegalArgumentException("Missing person cannot be null");
        }
        if (!isValidDateFormat(dateOfInquiry)) {
            throw new IllegalArgumentException("Invalid date of inquiry format. Use yyyy-MM-dd");
        }
        this.inquiryID = inquiryID;
        this.inquirer = inquirer;
        this.missingPerson = missingPerson;
        this.dateOfInquiry = dateOfInquiry;
        this.infoProvided = infoProvided;
        this.lastKnownLocation = lastKnownLocation;
    }

    /**
     * Gets the inquiry ID.
     *
     * @return the inquiry ID
     */
    public int getInquiryID() {
        return inquiryID;
    }

    /**
     * Gets the inquirer Person.
     *
     * @return the Person who inquired
     */
    public Person getInquirer() {
        return inquirer;
    }

    /**
     * Sets the inquirer.
     *
     * @param inquirer the new inquirer
     */
    public void setInquirer(Person inquirer) {
        if (inquirer == null) {
            throw new IllegalArgumentException("Inquirer cannot be null");
        }
        this.inquirer = inquirer;
    }

    /**
     * Gets the missing Person.
     *
     * @return the missing person
     */
    public Person getMissingPerson() {
        return missingPerson;
    }

    /**
     * Sets the missing Person.
     *
     * @param missingPerson the new missing person
     */
    public void setMissingPerson(Person missingPerson) {
        if (missingPerson == null) {
            throw new IllegalArgumentException("Missing person cannot be null");
        }
        this.missingPerson = missingPerson;
    }

    /**
     * Gets the date of inquiry.
     *
     * @return dateOfInquiry
     */
    public String getDateOfInquiry() {
        return dateOfInquiry;
    }

    /**
     * Sets the date of inquiry in yyyy-MM-dd format.
     *
     * @param dateOfInquiry the new date
     */
    public void setDateOfInquiry(String dateOfInquiry) {
        if (!isValidDateFormat(dateOfInquiry)) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
        }
        this.dateOfInquiry = dateOfInquiry;
    }

    /**
     * Gets the info provided.
     *
     * @return infoProvided
     */
    public String getInfoProvided() {
        return infoProvided;
    }

    /**
     * Sets additional info.
     *
     * @param infoProvided the new info
     */
    public void setInfoProvided(String infoProvided) {
        this.infoProvided = infoProvided;
    }

    /**
     * Gets the last known location.
     *
     * @return the location
     */
    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    /**
     * Sets the last known location.
     *
     * @param lastKnownLocation the new location
     */
    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    /**
     * Checks if the given date is yyyy-MM-dd.
     *
     * @param date the date string
     * @return true if valid, false otherwise
     */
    private boolean isValidDateFormat(String date) {
        if (date == null) return false;
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Creates a new inquiry row in the DB, letting the DB auto-generate inquiry_id.
     *
     * @param inquirerID person_id of the inquirer
     * @param missingID person_id of the missing person
     * @param locID location ID or null
     * @param dateStr date of inquiry
     * @param comments extra comments
     * @throws SQLException if insertion fails
     */
    public static void createInquiry(int inquirerID, int missingID, Integer locID,
                                     String dateStr, String comments) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "INSERT INTO inquiry (inquirer_id, seeking_id, location_id, date_of_inquiry, comments) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inquirerID);
            ps.setInt(2, missingID);
            if (locID == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, locID);
            }
            String finalDate = (dateStr == null || dateStr.isEmpty()) ? "2025-01-01" : dateStr;
            ps.setTimestamp(4, Timestamp.valueOf(finalDate + " 00:00:00"));
            ps.setString(5, (comments == null ? "" : comments));
            ps.executeUpdate();
        }
    }

    /**
     * Lists all inquiries in ascending order of inquiry_id.
     *
     * @throws SQLException if query fails
     */
    public static void listAllInquiriesSafe() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        String sql = "SELECT inquiry_id, inquirer_id, seeking_id, location_id, date_of_inquiry, comments "
                + "FROM inquiry ORDER BY inquiry_id ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int inqID   = rs.getInt("inquiry_id");
                int iqr     = rs.getInt("inquirer_id");
                int mis     = rs.getInt("seeking_id");
                Integer loc = (Integer) rs.getObject("location_id");
                String dt   = String.valueOf(rs.getTimestamp("date_of_inquiry"));
                String cmt  = rs.getString("comments");
                System.out.printf("Inquiry %d => inquirer:%d, missing:%d, loc:%s, date:%s, comment:%s\n",
                        inqID, iqr, mis, (loc==null?"null":loc), dt, cmt);
            }
        }
    }

    /**
     * Lists inquiries referencing a specific person as inquirer or missing.
     *
     * @param personId the ID of the person
     * @throws SQLException if query fails
     */
    public static void listForPerson(int personId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        System.out.println("\n= INQUIRIES =");
        String sql = "SELECT inquiry_id, inquirer_id, seeking_id, location_id, date_of_inquiry, comments "
                + "FROM inquiry WHERE inquirer_id=? OR seeking_id=? ORDER BY inquiry_id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personId);
            ps.setInt(2, personId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    int iq = rs.getInt("inquiry_id");
                    int iqr= rs.getInt("inquirer_id");
                    int mis= rs.getInt("seeking_id");
                    Integer loc = (Integer) rs.getObject("location_id");
                    String dt   = String.valueOf(rs.getTimestamp("date_of_inquiry"));
                    String cmt  = rs.getString("comments");
                    System.out.printf("InquiryID=%d => inquirer=%d, missing=%d, location=%s, date=%s, comments=%s\n",
                            iq, iqr, mis, (loc==null?"null":loc), dt, cmt);
                }
                if (!any) {
                    System.out.println("(None)");
                }
            }
        }
    }

    /**
     * Updates an existing inquiry row, only modifying non-empty fields.
     *
     * @param inquiryId the inquiry's ID
     * @param newInquirerId new inquirer ID or blank
     * @param newMissingId new missing ID or blank
     * @param newLocId new location ID or blank
     * @param newDate new date (yyyy-mm-dd) or blank
     * @param newComments new comments or blank
     * @throws SQLException if update fails
     */
    public static void updateInquiry(int inquiryId,
                                     String newInquirerId,
                                     String newMissingId,
                                     String newLocId,
                                     String newDate,
                                     String newComments) throws SQLException {
        Timestamp parsedDate = null;
        if (newDate != null && !newDate.isEmpty()) {
            parsedDate = Timestamp.valueOf(newDate + " 00:00:00");
        }

        StringBuilder sb = new StringBuilder("UPDATE inquiry SET ");
        boolean needComma = false;

        if (newInquirerId != null && !newInquirerId.isEmpty()) {
            sb.append("inquirer_id=?");
            needComma = true;
        }
        if (newMissingId != null && !newMissingId.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("seeking_id=?");
            needComma = true;
        }
        if (newLocId != null && !newLocId.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("location_id=?");
            needComma = true;
        }
        if (parsedDate != null) {
            if (needComma) sb.append(", ");
            sb.append("date_of_inquiry=?");
            needComma = true;
        }
        if (newComments != null && !newComments.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("comments=?");
            needComma = true;
        }

        if (!needComma) {
            System.out.println("No changes specified for inquiry.");
            return;
        }

        sb.append(" WHERE inquiry_id=?");
        String sql = sb.toString();

        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (newInquirerId != null && !newInquirerId.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(newInquirerId));
            }
            if (newMissingId != null && !newMissingId.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(newMissingId));
            }
            if (newLocId != null && !newLocId.isEmpty()) {
                int locVal = Integer.parseInt(newLocId);
                ps.setInt(idx++, locVal);
            }
            if (parsedDate != null) {
                ps.setTimestamp(idx++, parsedDate);
            }
            if (newComments != null && !newComments.isEmpty()) {
                ps.setString(idx++, newComments);
            }
            ps.setInt(idx, inquiryId);
            ps.executeUpdate();
        }
    }
}
