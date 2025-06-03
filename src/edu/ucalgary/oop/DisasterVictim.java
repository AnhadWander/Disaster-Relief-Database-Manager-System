package edu.ucalgary.oop;

import java.util.ArrayList;
import java.util.List;

/**
 * DisasterVictim extends Person if you want object references (medicalRecords, items, etc.)
 * This doesn't have to do direct DB logic, because Person does it for us via static methods.
 *  @author Anhad Wander
 *  @version 1.0
 *  @since 2025-04-05
 */
public class DisasterVictim extends Person {

    private String dateOfBirth;
    private Gender gender;
    private static int socialCounter = 1;
    private final int assignedSocialID;
    private final List<MedicalRecord> medicalRecords;
    private FamilyGroup familyGroup;
    private final List<Item> itemsAllocated;
    private final String entryDate;
    private String comments;

    public DisasterVictim(int personID, String firstName, String entryDate) {
        super(personID, firstName, "UnknownLast", "UnknownPhone");
        if (!isValidDateFormat(entryDate)) {
            throw new IllegalArgumentException("Invalid entry date format (yyyy-MM-dd).");
        }
        this.entryDate = entryDate;
        this.medicalRecords = new ArrayList<>();
        this.itemsAllocated = new ArrayList<>();
        this.assignedSocialID = socialCounter++;
    }

    public DisasterVictim(int personID, String firstName, String entryDate, String dateOfBirth) {
        this(personID, firstName, entryDate);
        if (!isValidDateFormat(dateOfBirth)) {
            throw new IllegalArgumentException("Invalid birth date format (yyyy-MM-dd).");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (!isValidDateFormat(dateOfBirth)) {
            throw new IllegalArgumentException("Invalid birth date format (yyyy-MM-dd).");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGenderEnum() {
        return gender;
    }

    public void setGenderEnum(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        this.gender = gender;
    }

    public int getAssignedSocialID() {
        return assignedSocialID;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord record) {
        this.medicalRecords.add(record);
    }

    public FamilyGroup getFamilyGroupObj() {
        return familyGroup;
    }

    public void setFamilyGroupObj(FamilyGroup familyGroup) {
        if (familyGroup == null) {
            throw new IllegalArgumentException("Family group cannot be null");
        }
        this.familyGroup = familyGroup;
    }

    public List<Item> getItemsAllocated() {
        return itemsAllocated;
    }

    public void allocateItem(Item item) {
        if (item.getAllocatedTo() != null && item.getAllocatedTo() != this) {
            throw new IllegalStateException("Item is already allocated to another victim");
        }
        if (item.getLocation() != null) {
            item.getLocation().removeItem(item);
        }
        item.setAllocatedTo(this);
        item.setLocation(null);
        this.itemsAllocated.add(item);
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private boolean isValidDateFormat(String date) {
        if (date == null) return false;
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
