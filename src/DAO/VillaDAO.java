package DAO;

import Model.Villa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillaDAO {

    public static List<Villa> getAll() {
        List<Villa> villas = new ArrayList<>();
        String sql = "SELECT * FROM villas";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                villas.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve villas", e);
        }

        return villas;
    }

    public static Villa getById(int id) {
        String sql = "SELECT * FROM villas WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get villa by ID", e);
        }

        return null;
    }

    public static Villa insert(Villa villa) {
        String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Inserting villa failed, no rows affected.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    villa.setId(keys.getInt(1));
                    return villa;
                } else {
                    throw new SQLException("Inserting villa failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert villa", e);
        }
    }

    public static boolean update(Villa villa) {
        String sql = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            stmt.setInt(4, villa.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update villa", e);
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM villas WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete villa", e);
        }
    }

    public static List<Villa> getAvailable(String ciDate, String coDate) {
        List<Villa> available = new ArrayList<>();

        String sql = "SELECT DISTINCT v.* " +
                "FROM villas v " +
                "WHERE v.id NOT IN ( " +
                "  SELECT rt.villa FROM bookings b " +
                "  JOIN room_types rt ON b.room_type = rt.id " +
                "  WHERE NOT (b.checkout_date <= ? OR b.checkin_date >= ?) " +
                ")";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ciDate);
            stmt.setString(2, coDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    available.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get available villas", e);
        }

        return available;
    }

    private static Villa mapRow(ResultSet rs) throws SQLException {
        return new Villa(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("address")
        );
    }
}
