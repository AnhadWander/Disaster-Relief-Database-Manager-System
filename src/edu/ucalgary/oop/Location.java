package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Location represents a shelter or place where victims and items can be allocated.
 * Provides logic for reassigning a person, searching details,
 * and static method to update the location name/address.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Location {
    private final int locationID;
    private String name;
    private String address;
    private final List<Item> items;
    private final List<DisasterVictim> occupants;

    /**
     * Constructs a Location with an ID, name, and address.
     *
     * @param locationID the location ID
     * @param name the location name
     * @param address the address
     */
    public Location(int locationID, String name, String address) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Location name cannot be null/empty");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Location address cannot be null/empty");
        }
        this.locationID = locationID;
        this.name = name;
        this.address = address;
        this.items = new ArrayList<>();
        this.occupants = new ArrayList<>();
    }

    /**
     * Gets the location ID.
     * @return the location ID
     */
    public int getLocationID() {
        return locationID;
    }

    /**
     * Gets the location name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the location name.
     * @param name new name
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Location name cannot be null/empty");
        }
        this.name = name;
    }

    /**
     * Gets the address.
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     * @param address new address
     */
    public void setAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null/empty");
        }
        this.address = address;
    }

    /**
     * Gets items in this location.
     * @return list of items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Adds an item to this location, removing from any victim if needed.
     */
    public void addItem(Item item) {
        if (item.getAllocatedTo() != null) {
            throw new IllegalStateException("Item is allocated to a victim, cannot add to location");
        }
        this.items.add(item);
        item.setLocation(this);
        item.setAllocatedTo(null);
    }

    /**
     * Removes an item from this location.
     */
    public void removeItem(Item item) {
        this.items.remove(item);
    }

    /**
     * Gets the occupant victims.
     */
    public List<DisasterVictim> getOccupants() {
        return occupants;
    }

    /**
     * Adds a victim occupant.
     */
    public void addOccupant(DisasterVictim victim) {
        this.occupants.add(victim);
    }

    /**
     * Removes a victim occupant.
     */
    public void removeOccupant(DisasterVictim victim) {
        this.occupants.remove(victim);
    }

    /**
     * Reassigns a person to a new location in personlocation table.
     */
    public static void reassignPersonToLocation(int personId, int locationId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        String del = "DELETE FROM personlocation WHERE person_id=?";
        try (PreparedStatement ps = conn.prepareStatement(del)) {
            ps.setInt(1, personId);
            ps.executeUpdate();
        }
        String ins = "INSERT INTO personlocation (person_id, location_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            ps.setInt(1, personId);
            ps.setInt(2, locationId);
            ps.executeUpdate();
        }
    }

    /**
     * Lists person->location mappings from DB.
     */
    public static void listPersonLocationsSafe() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        String sql = "SELECT person_id, location_id FROM personlocation ORDER BY person_id ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int p = rs.getInt("person_id");
                int l = rs.getInt("location_id");
                System.out.printf("Person %d => Location %d\n", p, l);
            }
        }
    }

    /**
     * Searches location details, printing occupant, supply, inquiry info.
     */
    public static void searchLocationDetailsSafe(int locId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        System.out.println("= LOCATION INFO =");
        String locSql = "SELECT location_id, name, address FROM location WHERE location_id=?";
        try (PreparedStatement ps = conn.prepareStatement(locSql)) {
            ps.setInt(1, locId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nm = rs.getString("name");
                    String ad = rs.getString("address");
                    System.out.printf("LocID=%d, Name=%s, Address=%s\n", locId, nm, ad);
                } else {
                    System.out.println("No location found with ID=" + locId);
                    return;
                }
            }
        }

        System.out.println("\n= OCCUPANTS (Persons) =");
        String occSql =
                "SELECT pl.person_id, p.first_name, p.last_name "
                        + "FROM personlocation pl JOIN person p ON pl.person_id=p.person_id "
                        + "WHERE pl.location_id=?";
        try (PreparedStatement ps = conn.prepareStatement(occSql)) {
            ps.setInt(1, locId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean anyOcc = false;
                while (rs.next()) {
                    anyOcc = true;
                    int pid = rs.getInt("person_id");
                    String fn= rs.getString("first_name");
                    String ln= rs.getString("last_name");
                    System.out.printf("PersonID=%d => %s %s\n", pid, fn, ln);
                }
                if (!anyOcc) System.out.println("(None)");
            }
        }

        System.out.println("\n= SUPPLIES ALLOCATED =");
        String supSql =
                "SELECT s.supply_id, s.type, s.comments "
                        + "FROM supplyallocation sa JOIN supply s ON sa.supply_id=s.supply_id "
                        + "WHERE sa.location_id=?";
        try (PreparedStatement ps = conn.prepareStatement(supSql)) {
            ps.setInt(1, locId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean anySup = false;
                while (rs.next()) {
                    anySup = true;
                    int sid = rs.getInt("supply_id");
                    String ty= rs.getString("type");
                    String cm= rs.getString("comments");
                    System.out.printf("SupplyID=%d, Type=%s, Comments=%s\n", sid, ty, cm);
                }
                if (!anySup) System.out.println("(None)");
            }
        }

        System.out.println("\n= INQUIRIES =");
        String inqSql = "SELECT inquiry_id, inquirer_id, seeking_id, date_of_inquiry, comments "
                + "FROM inquiry WHERE location_id=? ORDER BY inquiry_id ASC";
        try (PreparedStatement ps = conn.prepareStatement(inqSql)) {
            ps.setInt(1, locId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean anyInq = false;
                while (rs.next()) {
                    anyInq = true;
                    int iq    = rs.getInt("inquiry_id");
                    int inqer = rs.getInt("inquirer_id");
                    int missing = rs.getInt("seeking_id");
                    String dt = String.valueOf(rs.getTimestamp("date_of_inquiry"));
                    String cmt= rs.getString("comments");
                    System.out.printf("InquiryID=%d => inquirer=%d, missing=%d, date=%s, comments=%s\n",
                            iq, inqer, missing, dt, cmt);
                }
                if (!anyInq) {
                    System.out.println("(None)");
                }
            }
        }
        System.out.println("\n--- End of location details ---");
    }

    /**
     * Updates an existing location record (name/address) partially.
     *
     * @param locId the location's ID
     * @param newName new name or blank
     * @param newAddress new address or blank
     * @throws SQLException if update fails
     */
    public static void updateLocation(int locId, String newName, String newAddress) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE location SET ");
        boolean needComma = false;

        if (newName != null && !newName.isEmpty()) {
            sb.append("name=?");
            needComma = true;
        }
        if (newAddress != null && !newAddress.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("address=?");
            needComma = true;
        }
        if (!needComma) {
            System.out.println("No changes specified for location.");
            return;
        }
        sb.append(" WHERE location_id=?");

        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            int idx = 1;
            if (newName != null && !newName.isEmpty()) {
                ps.setString(idx++, newName);
            }
            if (newAddress != null && !newAddress.isEmpty()) {
                ps.setString(idx++, newAddress);
            }
            ps.setInt(idx, locId);
            ps.executeUpdate();
        }
    }
}
