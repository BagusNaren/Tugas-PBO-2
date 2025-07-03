package DAO;

import Model.Voucher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    public static List<Voucher> getAll() {
        List<Voucher> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";

        try (
                Connection conn = Database.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                vouchers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vouchers;
    }

    public static Voucher getById(int id) {
        String sql = "SELECT * FROM vouchers WHERE id = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insert(Voucher voucher) {
        String sql = "INSERT INTO vouchers (code, description, discount, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, voucher.getCode());
            stmt.setString(2, voucher.getDescription());
            stmt.setDouble(3, voucher.getDiscount());
            stmt.setString(4, voucher.getStartDate());
            stmt.setString(5, voucher.getEndDate());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting voucher failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voucher.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Inserting voucher failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Voucher voucher) {
        String sql = "UPDATE vouchers SET code = ?, description = ?, discount = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, voucher.getCode());
            stmt.setString(2, voucher.getDescription());
            stmt.setDouble(3, voucher.getDiscount());
            stmt.setString(4, voucher.getStartDate());
            stmt.setString(5, voucher.getEndDate());
            stmt.setInt(6, voucher.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating voucher failed, no rows affected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM vouchers WHERE id = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Voucher mapRow(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("description"),
                rs.getDouble("discount"),
                rs.getString("start_date"),
                rs.getString("end_date")
        );
    }
}
