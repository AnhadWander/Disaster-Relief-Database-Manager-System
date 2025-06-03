package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Water is an Item that expires after one day if allocated to a person.
 *
 * @author Anhad Wander
 * @version 1.0
 * @since 2025-04-05
 */
public class Water extends Item {

    /**
     * Constructs a Water item with the given ID.
     *
     * @param itemID the ID
     * @throws IllegalArgumentException if negative
     */
    public Water(int itemID) {
        super(itemID);
    }

    /**
     * Removes expired water allocations from the supplyallocation table
     * if allocated to a person over one day.
     *
     * @throws SQLException if DB update fails
     */
    public static void removeExpiredAllocations() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        System.out.println("");
        String sql =
                "DELETE FROM supplyallocation sa " +
                        "USING supply s " +
                        "WHERE sa.supply_id = s.supply_id " +
                        "  AND s.type = 'water' " +
                        "  AND sa.person_id IS NOT NULL " +
                        "  AND sa.allocation_date < (NOW() - INTERVAL '1 day')";
        try (Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("Removed " + rows + " expired water allocations.");
            } else {
                System.out.println("No expired water found.");
            }
        }
    }
}
