package DAO;

import Model.Villa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillaDAO {

    public static List<Villa> getAll() {
        List<Villa> villas = new ArrayList<>();
        System.out.println(">> DAO: Memanggil getAll()");

        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM villas")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String address = rs.getString("address");

                System.out.printf(">> Villa DB: id=%d, name=%s%n", id, name);

                Villa villa = new Villa(id, name, description, address);
                villas.add(villa);
            }

            System.out.println(">> DAO: Total villa ditemukan: " + villas.size());

        } catch (SQLException e) {
            System.err.println(">> DAO ERROR: Gagal ambil data villa");
            e.printStackTrace();
            throw new RuntimeException("Gagal query SELECT * FROM villas", e);
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
                return new Villa(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void insert(Villa villa) {
        String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, villa.getName());
            stmt.setString(2, villa.getDescription());
            stmt.setString(3, villa.getAddress());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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
        }

        return false;
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM villas WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Villa> getAvailable(String ciDate, String coDate) throws SQLException {
        List<Villa> available = new ArrayList<>();

        Connection conn = Database.connect();
        String sql = "SELECT DISTINCT v.* " +
                "FROM villas v " +
                "WHERE v.id NOT IN ( " +
                "  SELECT rt.villa FROM bookings b " +
                "  JOIN room_types rt ON b.room_type = rt.id " +
                "  WHERE NOT (b.checkout_date <= ? OR b.checkin_date >= ?) " +
                ")";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, ciDate); // checkout_date <= checkin permintaan → tidak bentrok
        stmt.setString(2, coDate); // checkin_date >= checkout permintaan → tidak bentrok

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Villa v = new Villa(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("address")
            );
            available.add(v);
        }

        rs.close();
        stmt.close();
        conn.close();

        return available;
    }
}
