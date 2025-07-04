package Handler;

import MainApp.Main;
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
import Http.Request;
import Http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class VillaHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handle(HttpExchange exchange) throws IOException {
        Request req = new Request(exchange);
        Response res = new Response(exchange);

        String clientKey = exchange.getRequestHeaders().getFirst("X-API-Key");
        if (clientKey == null || !clientKey.equals(Main.API_KEY)) {
            res.setBody(jsonError("Unauthorized: Invalid or missing API key"));
            res.send(HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    handleGet(parts, req, res);
                    break;
                case "POST":
                    handlePost(parts, req, res);
                    break;
                case "PUT":
                    handlePut(parts, req, res);
                    break;
                case "DELETE":
                    handleDelete(parts, res);
                    break;
                default:
                    throw new MethodNotAllowedException("Method " + method + " not allowed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setBody(jsonError("Internal server error"));
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
                if (available.isEmpty()) throw new NotFoundException("No available villas found");
                res.setBody(jsonResponse("Available villas retrieved successfully", available));
            } else {
                List<Villa> villas = VillaDAO.getAll();
                if (villas.isEmpty()) throw new NotFoundException("No villas found");
                res.setBody(jsonResponse("Villas retrieved successfully", villas));
            }
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 3) {
            int id = parseIdOrThrow(parts[2], "villa");
            Villa villa = VillaDAO.getById(id);
            if (villa == null) throw new NotFoundException("Villa not found");
            res.setBody(jsonResponse("Villa retrieved successfully", villa));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 4) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            String subPath = parts[3];
            if ("rooms".equals(subPath)) {
                List<RoomType> rooms = RoomTypesDAO.getByVillaId(villaId);
                if (rooms.isEmpty()) throw new NotFoundException("No rooms found");
                res.setBody(jsonResponse("Rooms retrieved successfully", rooms));
            } else if ("bookings".equals(subPath)) {
                List<Booking> bookings = BookingDAO.getAllByVilla(villaId);
                if (bookings.isEmpty()) throw new NotFoundException("No bookings found");
                res.setBody(jsonResponse("Bookings retrieved successfully", bookings));
            } else if ("reviews".equals(subPath)) {
                List<Review> reviews = ReviewDAO.getAllByVillaId(villaId);
                if (reviews.isEmpty()) throw new NotFoundException("No reviews found");
                res.setBody(jsonResponse("Reviews retrieved successfully", reviews));
            } else {
                throw new NotFoundException("Unknown GET path");
            }
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid GET path");
        }
    }

    private static void handlePost(String[] parts, Request req, Response res) throws SQLException, IOException {
        String body = req.getBody();
        if (parts.length == 2) {
            Villa villa = objectMapper.readValue(body, Villa.class);
            validateVilla(villa);
            VillaDAO.insert(villa);
            res.setBody(jsonResponse("Villa added successfully", villa));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 4 && "rooms".equals(parts[3])) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            RoomType room = objectMapper.readValue(body, RoomType.class);
            room.setVilla(villaId);
            RoomTypesDAO.insert(room);
            res.setBody(jsonResponse("Room added successfully", room));
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
            validateVilla(villa);
            boolean updated = VillaDAO.update(villa);
            if (!updated) throw new NotFoundException("Villa not found");
            res.setBody(jsonResponse("Villa updated successfully", villa));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 5 && "rooms".equals(parts[3])) {
            int villaId = parseIdOrThrow(parts[2], "villa");
            int roomId = parseIdOrThrow(parts[4], "room");
            RoomType room = objectMapper.readValue(body, RoomType.class);
            room.setId(roomId);
            room.setVilla(villaId);
            boolean updated = RoomTypesDAO.update(room);
            if (!updated) throw new NotFoundException("Room not found");
            res.setBody(jsonResponse("Room updated successfully", room));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    private static void handleDelete(String[] parts, Response res) throws SQLException, IOException {
        if (parts.length == 3) {
            int id = parseIdOrThrow(parts[2], "villa");
            boolean deleted = VillaDAO.delete(id);
            if (!deleted) throw new NotFoundException("Villa not found");
            res.setBody(jsonResponse("Villa deleted successfully", null));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 5 && "rooms".equals(parts[3])) {
            int roomId = parseIdOrThrow(parts[4], "room");
            boolean deleted = RoomTypesDAO.delete(roomId);
            if (!deleted) throw new NotFoundException("Room not found");
            res.setBody(jsonResponse("Room deleted successfully", null));
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

    private static void validateVilla(Villa v) {
        if (v.getName() == null || v.getName().isBlank()) {
            throw new BadRequestException("Villa name is required");
        }
        if (v.getName().length() > 100) {
            throw new BadRequestException("Villa name must not exceed 100 characters");
        }
        if (v.getDescription() == null || v.getDescription().isBlank()) {
            throw new BadRequestException("Villa description is required");
        }
        if (v.getDescription().length() > 500) {
            throw new BadRequestException("Villa description must not exceed 500 characters");
        }
        if (v.getAddress() == null || v.getAddress().isBlank()) {
            throw new BadRequestException("Villa address is required");
        }
        if (v.getAddress().length() > 255) {
            throw new BadRequestException("Villa address must not exceed 255 characters");
        }
    }

    private static String jsonResponse(String message, Object data) throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("message", message);
        result.put("data", data);
        return objectMapper.writeValueAsString(result);
    }

    private static String jsonError(String msg) throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("error", msg);
        return objectMapper.writeValueAsString(result);
    }
}
