package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * MedicalRecord represents a person's medical treatment info,
 * including location, date, and details.
 * Also has static DB logic for insert, listForPerson, update.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class MedicalRecord {
    private final int recordID;
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;

    /**
     * Constructs a MedicalRecord with the specified fields.
     *
     * @param recordID the medical record ID
     * @param location the location
     * @param treatmentDetails the details
     * @param dateOfTreatment yyyy-MM-dd format
     * @throws IllegalArgumentException if date is invalid or details empty
     */
    public MedicalRecord(int recordID, Location location, String treatmentDetails, String dateOfTreatment) {
        if (!isValidDateFormat(dateOfTreatment)) {
            throw new IllegalArgumentException("Invalid date format for treatment date. Use yyyy-MM-dd");
        }
        this.recordID = recordID;
        this.location = location;
        if (treatmentDetails == null || treatmentDetails.isEmpty()) {
            throw new IllegalArgumentException("Treatment details cannot be null/empty");
        }
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
    }

    /**
     * Gets the medical record ID.
     *
     * @return the record ID
     */
    public int getRecordID() {
        return recordID;
    }

    /**
     * Gets the location of treatment.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of treatment.
     *
     * @param location the new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the treatment details.
     *
     * @return the details
     */
    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    /**
     * Sets the treatment details.
     *
     * @param treatmentDetails new details
     * @throws IllegalArgumentException if empty
     */
    public void setTreatmentDetails(String treatmentDetails) {
        if (treatmentDetails == null || treatmentDetails.isEmpty()) {
            throw new IllegalArgumentException("Treatment details cannot be null/empty");
        }
        this.treatmentDetails = treatmentDetails;
    }

    /**
     * Gets the date of treatment in yyyy-MM-dd format.
     *
     * @return the date
     */
    public String getDateOfTreatment() {
        return dateOfTreatment;
    }

    /**
     * Sets the date of treatment.
     *
     * @param dateOfTreatment new date yyyy-MM-dd
     * @throws IllegalArgumentException if invalid format
     */
    public void setDateOfTreatment(String dateOfTreatment) {
        if (!isValidDateFormat(dateOfTreatment)) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
        }
        this.dateOfTreatment = dateOfTreatment;
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
     * Inserts a new medical record row into the DB.
     *
     * @param personId the person ID
     * @param locationId the location ID
     * @param dateStr the date of treatment
     * @param details the treatment details
     * @throws SQLException if insert fails
     */
    public static void insertRecord(int personId, int locationId, String dateStr, String details) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "INSERT INTO medicalrecord (location_id, person_id, date_of_treatment, treatment_details) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            ps.setInt(2, personId);
            if (dateStr == null || dateStr.isEmpty()) {
                ps.setTimestamp(3, null);
            } else {
                ps.setTimestamp(3, Timestamp.valueOf(dateStr + " 00:00:00"));
            }
            ps.setString(4, (details==null?"":details));
            ps.executeUpdate();
        }
    }

    /**
     * Lists all medical records for a given person.
     *
     * @param personId the person ID
     * @throws SQLException if query fails
     */
    public static void listForPerson(int personId) throws SQLException {
        System.out.println("\n= MEDICAL RECORDS =");
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        String sql = "SELECT medical_record_id, location_id, date_of_treatment, treatment_details FROM medicalrecord WHERE person_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean anyMed = false;
                while (rs.next()) {
                    anyMed = true;
                    int mrID  = rs.getInt("medical_record_id");
                    int locID = rs.getInt("location_id");
                    String dt = String.valueOf(rs.getTimestamp("date_of_treatment"));
                    String ds = rs.getString("treatment_details");
                    System.out.printf("RecordID=%d, Location=%d, Date=%s, Details=%s\n",
                            mrID, locID, dt, ds);
                }
                if (!anyMed) System.out.println("(None)");
            }
        }
    }

    /**
     * Updates an existing medical record row, only modifying non-empty fields.
     *
     * @param recordId the record ID
     * @param newLocationId new location ID or blank
     * @param newDateStr new date or blank
     * @param newDetails new details or blank
     * @throws SQLException if update fails
     */
    public static void updateMedicalRecord(int recordId,
                                           String newLocationId,
                                           String newDateStr,
                                           String newDetails) throws SQLException {
        Timestamp parsedDate = null;
        if (newDateStr != null && !newDateStr.isEmpty()) {
            parsedDate = Timestamp.valueOf(newDateStr + " 00:00:00");
        }

        StringBuilder sb = new StringBuilder("UPDATE medicalrecord SET ");
        boolean needComma = false;

        if (newLocationId != null && !newLocationId.isEmpty()) {
            sb.append("location_id=?");
            needComma = true;
        }
        if (parsedDate != null) {
            if (needComma) sb.append(", ");
            sb.append("date_of_treatment=?");
            needComma = true;
        }
        if (newDetails != null && !newDetails.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("treatment_details=?");
            needComma = true;
        }

        if (!needComma) {
            System.out.println("No changes specified for medical record.");
            return;
        }

        sb.append(" WHERE medical_record_id=?");
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            int idx = 1;
            if (newLocationId != null && !newLocationId.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(newLocationId));
            }
            if (parsedDate != null) {
                ps.setTimestamp(idx++, parsedDate);
            }
            if (newDetails != null && !newDetails.isEmpty()) {
                ps.setString(idx++, newDetails);
            }
            ps.setInt(idx, recordId);
            ps.executeUpdate();
        }
    }
}
