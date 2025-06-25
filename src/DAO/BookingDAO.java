package DAO;

import Model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public static List<Booking> getAllByCustomer(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer = ?";
        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extract(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static List<Booking> getAllByVilla(int villaId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.* FROM bookings b " +
                "JOIN room_types rt ON b.room_type = rt.id " +
                "WHERE rt.villa = ?";
        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extract(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static Booking getById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extract(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insert(Booking b) {
        String sql = "INSERT INTO bookings " +
                "(customer, room_type, checkin_date, checkout_date, " +
                "price, voucher, final_price, payment_status, has_checkedin, has_checkedout) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, b.getCustomerId());
            stmt.setInt(2, b.getRoomTypeId());
            stmt.setString(3, b.getCheckinDate());
            stmt.setString(4, b.getCheckoutDate());
            stmt.setInt(5, b.getPrice());
            stmt.setInt(6, b.getVoucherId());
            stmt.setInt(7, b.getFinalPrice());
            stmt.setString(8, b.getPaymentStatus());
            stmt.setInt(9, b.isCheckedIn() ? 1 : 0);
            stmt.setInt(10, b.isCheckedOut() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateStatus(int bookingId, boolean checkin, boolean checkout, String status) {
        String sql = "UPDATE bookings SET has_checkedin = ?, has_checkedout = ?, payment_status = ? WHERE id = ?";
        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, checkin ? 1 : 0);
            stmt.setInt(2, checkout ? 1 : 0);
            stmt.setString(3, status);
            stmt.setInt(4, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Booking extract(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("id"),
                rs.getInt("customer"),
                rs.getInt("room_type"),
                rs.getString("checkin_date"),
                rs.getString("checkout_date"),
                rs.getInt("price"),
                rs.getInt("voucher"),
                rs.getInt("final_price"),
                rs.getString("payment_status"),
                rs.getInt("has_checkedin") == 1,
                rs.getInt("has_checkedout") == 1
        );
    }
}
