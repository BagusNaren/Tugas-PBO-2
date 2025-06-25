package Handler;

import DAO.BookingDAO;
import DAO.ReviewDAO;
import DAO.RoomTypesDAO;
import DAO.VillaDAO;
import Exception.BadRequestException;
import Exception.MethodNotAllowedException;
import Exception.NotFoundException;
import Model.Booking;
import Model.Review;
import Model.RoomType;
import Model.Villa;
import Tugas2.Request;
import Tugas2.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VillaHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handle(HttpExchange exchange) throws IOException {
        Request req = new Request(exchange);
        Response res = new Response(exchange);

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        try {
            if (method.equals("GET")) {
                handleGet(parts, req, res);
            } else if (method.equals("POST")) {
                handlePost(parts, req, res);
            } else if (method.equals("PUT")) {
                handlePut(parts, req, res);
            } else if (method.equals("DELETE")) {
                handleDelete(parts, res);
            } else {
                throw new MethodNotAllowedException("Method " + method + " not allowed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setBody("{\"error\": \"Internal server error\"}");
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_NOT_FOUND);
        } catch (BadRequestException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (MethodNotAllowedException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_BAD_METHOD);
        } catch (Exception e) {
            e.printStackTrace();
            res.setBody(jsonError("Unexpected error: " + e.getMessage()));
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    private static void handleGet(String[] parts, Request req, Response res) throws SQLException, IOException {
        if (parts.length == 2) {
            Map<String, String> q = req.getQueryParams();
            if (q.containsKey("ci_date") && q.containsKey("co_date")) {
                List<Villa> available = VillaDAO.getAvailable(q.get("ci_date"), q.get("co_date"));
                res.setBody(objectMapper.writeValueAsString(available));
            } else {
                List<Villa> villas = VillaDAO.getAll();
                res.setBody(objectMapper.writeValueAsString(villas));
            }
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 3) {
            int id = parseIdOrThrow(parts[2], "villa");
            Villa villa = VillaDAO.getById(id);
            if (villa == null) throw new NotFoundException("Villa not found");
            res.setBody(objectMapper.writeValueAsString(villa));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 4) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            String subPath = parts[3];
            if ("rooms".equals(subPath)) {
                List<RoomType> rooms = RoomTypesDAO.getByVillaId(villaId);
                res.setBody(objectMapper.writeValueAsString(rooms));
                res.send(HttpURLConnection.HTTP_OK);
            } else if ("bookings".equals(subPath)) {
                List<Booking> bookings = BookingDAO.getAllByVilla(villaId);
                res.setBody(objectMapper.writeValueAsString(bookings));
                res.send(HttpURLConnection.HTTP_OK);
            } else if ("reviews".equals(subPath)) {
                List<Review> reviews = ReviewDAO.getAllByVillaId(villaId);
                res.setBody(objectMapper.writeValueAsString(reviews));
                res.send(HttpURLConnection.HTTP_OK);
            } else {
                throw new NotFoundException("Unknown GET path");
            }
        } else {
            throw new NotFoundException("Invalid GET path");
        }
    }

    private static void handlePost(String[] parts, Request req, Response res) throws SQLException, IOException {
        String body = req.getBody();
        if (parts.length == 2) {
            Villa villa = objectMapper.readValue(body, Villa.class);
            if (isInvalid(villa)) throw new BadRequestException("Invalid villa data");
            VillaDAO.insert(villa);
            res.setBody(objectMapper.writeValueAsString(villa));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 4 && "rooms".equals(parts[3])) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            RoomType room = objectMapper.readValue(body, RoomType.class);
            room.setVilla(villaId);
            RoomTypesDAO.insert(room);
            res.setBody(objectMapper.writeValueAsString(room));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else {
            throw new NotFoundException("Invalid POST path");
        }
    }

    private static void handlePut(String[] parts, Request req, Response res) throws SQLException, IOException {
        String body = req.getBody();
        if (parts.length == 3) {
            int id = parseIdOrThrow(parts[2], "villa");
            Villa villa = objectMapper.readValue(body, Villa.class);
            villa.setId(id);
            if (isInvalid(villa)) throw new BadRequestException("Invalid villa data");
            boolean updated = VillaDAO.update(villa);
            if (!updated) throw new NotFoundException("Villa not found");
            res.setBody(objectMapper.writeValueAsString(villa));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 5 && "rooms".equals(parts[3])) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            int roomId = parseIdOrThrow(parts[4], "room");
            RoomType room = objectMapper.readValue(body, RoomType.class);
            room.setId(roomId);
            room.setVilla(villaId);
            boolean updated = RoomTypesDAO.update(room);
            if (!updated) throw new NotFoundException("Room not found");
            res.setBody(objectMapper.writeValueAsString(room));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    private static void handleDelete(String[] parts, Response res) throws SQLException {
        if (parts.length == 3) {
            int id = parseIdOrThrow(parts[2], "villa");
            boolean deleted = VillaDAO.delete(id);
            if (!deleted) throw new NotFoundException("Villa not found");
            res.setBody(jsonMessage("Villa deleted"));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 5 && "rooms".equals(parts[3])) {
            int roomId = parseIdOrThrow(parts[4], "room");
            boolean deleted = RoomTypesDAO.delete(roomId);
            if (!deleted) throw new NotFoundException("Room not found");
            res.setBody(jsonMessage("Room deleted"));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid DELETE path");
        }
    }

    private static int parseIdOrThrow(String str, String label) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid " + label + " ID");
        }
    }

    private static boolean isInvalid(Villa v) {
        return v.getName() == null || v.getName().isBlank() ||
                v.getDescription() == null || v.getDescription().isBlank() ||
                v.getAddress() == null || v.getAddress().isBlank();
    }

    private static String jsonError(String msg) {
        return "{\"error\": \"" + msg.replace("\"", "'") + "\"}";
    }

    private static String jsonMessage(String msg) {
        return "{\"message\": \"" + msg.replace("\"", "'") + "\"}";
    }
}
