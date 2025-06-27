package DAO;

import Model.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypesDAO {

    public static boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM room_types WHERE id = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean update(RoomType room) {
        String sql = "UPDATE room_types SET " +
                "villa = ?, name = ?, quantity = ?, capacity = ?, price = ?, " +
                "bed_size = ?, has_desk = ?, has_ac = ?, has_tv = ?, has_wifi = ?, " +
                "has_shower = ?, has_hotwater = ?, has_fridge = ? " +
                "WHERE id = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, room.getVilla());
            stmt.setString(2, room.getName());
            stmt.setInt(3, room.getQuantity());
            stmt.setInt(4, room.getCapacity());
            stmt.setInt(5, room.getPrice());
            stmt.setString(6, room.getBedSize());
            stmt.setInt(7, room.isHasDesk() ? 1 : 0);
            stmt.setInt(8, room.isHasAc() ? 1 : 0);
            stmt.setInt(9, room.isHasTv() ? 1 : 0);
            stmt.setInt(10, room.isHasWifi() ? 1 : 0);
            stmt.setInt(11, room.isHasShower() ? 1 : 0);
            stmt.setInt(12, room.isHasHotwater() ? 1 : 0);
            stmt.setInt(13, room.isHasFridge() ? 1 : 0);
            stmt.setInt(14, room.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insert(RoomType room) throws SQLException {
        String sql = "INSERT INTO room_types (" +
                "villa, name, quantity, capacity, price, bed_size, " +
                "has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, room.getVilla());
            stmt.setString(2, room.getName());
            stmt.setInt(3, room.getQuantity());
            stmt.setInt(4, room.getCapacity());
            stmt.setInt(5, room.getPrice());
            stmt.setString(6, room.getBedSize());
            stmt.setInt(7, room.isHasDesk() ? 1 : 0);
            stmt.setInt(8, room.isHasAc() ? 1 : 0);
            stmt.setInt(9, room.isHasTv() ? 1 : 0);
            stmt.setInt(10, room.isHasWifi() ? 1 : 0);
            stmt.setInt(11, room.isHasShower() ? 1 : 0);
            stmt.setInt(12, room.isHasHotwater() ? 1 : 0);
            stmt.setInt(13, room.isHasFridge() ? 1 : 0);

            stmt.executeUpdate();
        }
    }

    public static List<RoomType> getByVillaId(int villaId) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM room_types WHERE villa = ?";

        try (
                Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RoomType r = new RoomType();
                r.setId(rs.getInt("id"));
                r.setVilla(rs.getInt("villa"));
                r.setName(rs.getString("name"));
                r.setQuantity(rs.getInt("quantity"));
                r.setCapacity(rs.getInt("capacity"));
                r.setPrice(rs.getInt("price"));
                r.setBedSize(rs.getString("bed_size"));
                r.setHasDesk(rs.getInt("has_desk") == 1);
                r.setHasAc(rs.getInt("has_ac") == 1);
                r.setHasTv(rs.getInt("has_tv") == 1);
                r.setHasWifi(rs.getInt("has_wifi") == 1);
                r.setHasShower(rs.getInt("has_shower") == 1);
                r.setHasHotwater(rs.getInt("has_hotwater") == 1);
                r.setHasFridge(rs.getInt("has_fridge") == 1);

                list.add(r);
            }
        }

        return list;
    }
}
