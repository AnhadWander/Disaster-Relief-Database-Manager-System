package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Abstract Item class, representing an item that may be allocated
 * to a disaster victim or a location. Also includes static DB logic
 * to manage supply rows (create, allocate, update, list).
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public abstract class Item {
    private final int itemID;
    private DisasterVictim allocatedTo;
    private Location location;

    /**
     * Constructs an Item with a specific ID.
     *
     * @param itemID the ID
     * @throws IllegalArgumentException if itemID is negative
     */
    public Item(int itemID) {
        if (itemID < 0) {
            throw new IllegalArgumentException("Item ID cannot be negative");
        }
        this.itemID = itemID;
    }

    /**
     * Gets the item ID.
     *
     * @return the item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Gets the DisasterVictim to whom this item is allocated.
     *
     * @return the allocated victim, or null if none
     */
    public DisasterVictim getAllocatedTo() {
        return allocatedTo;
    }

    /**
     * Sets the allocated DisasterVictim for this item.
     *
     * @param victim the victim, or null
     */
    public void setAllocatedTo(DisasterVictim victim) {
        this.allocatedTo = victim;
    }

    /**
     * Gets the Location of this item, if any.
     *
     * @return the Location, or null
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the Location of this item.
     *
     * @param location the new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Creates (inserts) a new supply row in the DB table "supply".
     *
     * @param type     the supply type
     * @param comments optional comments (can be null)
     * @throws SQLException if DB insert fails
     */
    public static void createSupply(String type, String comments) throws SQLException {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Supply type cannot be null or empty");
        }
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        String sql = "INSERT INTO supply (type, comments) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            if (comments == null) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, comments);
            }
            ps.executeUpdate();
        }
    }

    /**
     * Lists all supplies from the DB.
     *
     * @throws SQLException if query fails
     */
    public static void listAllSuppliesSafe() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        String sql = "SELECT supply_id, type, comments FROM supply ORDER BY supply_id ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int supID = rs.getInt("supply_id");
                String t   = rs.getString("type");
                String cmt = rs.getString("comments");
                System.out.printf("SupplyID=%d, Type=%s, Comments=%s\n", supID, t, cmt);
            }
        }
    }

    /**
     * Lists supplies allocated to a specific person.
     *
     * @param personId the person's ID
     * @throws SQLException if query fails
     */
    public static void listSuppliesForPerson(int personId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        System.out.println("\n= SUPPLIES ALLOCATED =");
        String sql = "SELECT s.supply_id, s.type, s.comments "
                + "FROM supplyallocation sa JOIN supply s ON sa.supply_id=s.supply_id "
                + "WHERE sa.person_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean anySup = false;
                while (rs.next()) {
                    anySup = true;
                    int sid = rs.getInt("supply_id");
                    String ty= rs.getString("type");
                    String cm= rs.getString("comments");
                    System.out.printf("SupplyID=%d, Type=%s, Comments=%s\n", sid, ty, cm);
                }
                if (!anySup) {
                    System.out.println("(None)");
                }
            }
        }
    }

    /**
     * Allocates an existing supply to a person, removing it from a location if needed.
     *
     * @param supplyId the supply ID
     * @param personId the person's ID
     * @throws SQLException if DB operations fail
     */
    public static void allocateToPerson(int supplyId, int personId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        String updateSql =
                "UPDATE supplyallocation SET person_id=?, location_id=NULL, allocation_date=NOW() "
                        + "WHERE supply_id=? AND person_id IS NULL AND location_id IS NOT NULL";
        int updatedCount;
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setInt(1, personId);
            pstmt.setInt(2, supplyId);
            updatedCount = pstmt.executeUpdate();
        }
        if (updatedCount == 0) {
            String insertSql = "INSERT INTO supplyallocation (supply_id, person_id, location_id) VALUES (?, ?, NULL)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, supplyId);
                pstmt.setInt(2, personId);
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Allocates a supply to a location.
     *
     * @param supplyId   the supply ID
     * @param locationId the location ID
     * @throws SQLException if DB operations fail
     */
    public static void allocateToLocation(int supplyId, int locationId) throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");
        String sql = "INSERT INTO supplyallocation (supply_id, location_id, person_id) VALUES (?, ?, NULL)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplyId);
            ps.setInt(2, locationId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates the type/comments of an existing supply in DB.
     *
     * @param supplyId the supply ID
     * @param newType the new type or blank
     * @param newComments the new comments or blank
     * @throws SQLException if update fails
     */
    public static void updateSupply(int supplyId, String newType, String newComments) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE supply SET ");
        boolean needComma = false;

        if (newType != null && !newType.isEmpty()) {
            sb.append("type=?");
            needComma = true;
        }
        if (newComments != null && !newComments.isEmpty()) {
            if (needComma) sb.append(", ");
            sb.append("comments=?");
            needComma = true;
        }
        if (!needComma) {
            System.out.println("No changes specified for supply.");
            return;
        }
        sb.append(" WHERE supply_id=?");

        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) throw new IllegalStateException("DB not connected");

        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            int idx = 1;
            if (newType != null && !newType.isEmpty()) {
                ps.setString(idx++, newType);
            }
            if (newComments != null && !newComments.isEmpty()) {
                ps.setString(idx++, newComments);
            }
            ps.setInt(idx, supplyId);
            ps.executeUpdate();
        }
    }
}
