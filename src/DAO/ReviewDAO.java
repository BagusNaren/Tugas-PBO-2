package DAO;

import Model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public static Review getByBookingId(int bookingId) {
        String sql = "SELECT * FROM reviews WHERE booking = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Review> getAllByVillaId(int villaId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.booking, r.star, r.title, r.content " +
                "FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "JOIN room_types rt ON b.room_type = rt.id " +
                "WHERE rt.villa = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static List<Review> getAllByCustomerId(int customerId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.booking, r.star, r.title, r.content " +
                "FROM reviews r " +
                "JOIN bookings b ON r.booking = b.id " +
                "WHERE b.customer = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("booking"),
                        rs.getInt("star"),
                        rs.getString("title"),
                        rs.getString("content")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static void insert(Review review) {
        String sql = "INSERT INTO reviews (booking, star, title, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, review.getBookingId());
            stmt.setInt(2, review.getStar());
            stmt.setString(3, review.getTitle());
            stmt.setString(4, review.getContent());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
