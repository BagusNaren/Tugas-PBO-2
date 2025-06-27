package Handler;

import DAO.VoucherDAO;
import Model.Voucher;
import Http.Request;
import Http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import Exception.MethodNotAllowedException;
import Exception.NotFoundException;
import Exception.BadRequestException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public class VoucherHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handle(HttpExchange exchange) throws IOException {
        Request req = new Request(exchange);
        Response res = new Response(exchange);

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    handleGet(parts, res);
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

        } catch (BadRequestException | IllegalArgumentException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (NotFoundException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_NOT_FOUND);
        } catch (MethodNotAllowedException e) {
            res.setBody(jsonError(e.getMessage()));
            res.send(HttpURLConnection.HTTP_BAD_METHOD);
        } catch (Exception e) {
            e.printStackTrace();
            res.setBody(jsonError("Internal server error: " + e.getMessage()));
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    // ===================== GET =====================
    private static void handleGet(String[] parts, Response res) throws IOException {
        if (parts.length == 2 && parts[1].equals("vouchers")) {
            List<Voucher> vouchers = VoucherDAO.getAll();
            res.setBody(objectMapper.writeValueAsString(vouchers));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 3 && parts[1].equals("vouchers")) {
            int id = parseIdOrThrow(parts[2], "voucher");
            Voucher voucher = VoucherDAO.getById(id);
            if (voucher == null) {
                throw new NotFoundException("Voucher with ID " + id + " not found");
            }
            res.setBody(objectMapper.writeValueAsString(voucher));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid GET path");
        }
    }

    // ===================== POST =====================
    private static void handlePost(String[] parts, Request req, Response res) throws IOException {
        if (parts.length == 2 && parts[1].equals("vouchers")) {
            Voucher voucher = objectMapper.readValue(req.getBody(), Voucher.class);
            if (voucher.getCode() == null || voucher.getCode().isEmpty()) {
                throw new BadRequestException("Voucher code cannot be empty");
            }
            VoucherDAO.insert(voucher);
            res.setBody(objectMapper.writeValueAsString(voucher));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else {
            throw new NotFoundException("Invalid POST path");
        }
    }

    // ===================== PUT =====================
    private static void handlePut(String[] parts, Request req, Response res) throws IOException {
        if (parts.length == 3 && parts[1].equals("vouchers")) {
            int id = parseIdOrThrow(parts[2], "voucher");
            Voucher voucher = objectMapper.readValue(req.getBody(), Voucher.class);
            if (voucher.getCode() == null || voucher.getCode().isEmpty()) {
                throw new BadRequestException("Voucher code cannot be empty");
            }
            voucher.setId(id);
            VoucherDAO.update(voucher);
            res.setBody(objectMapper.writeValueAsString(voucher));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    // ===================== DELETE =====================
    private static void handleDelete(String[] parts, Response res) throws IOException {
        if (parts.length == 3 && parts[1].equals("vouchers")) {
            int id = parseIdOrThrow(parts[2], "voucher");
            VoucherDAO.delete(id);
            res.setBody(jsonMessage("Voucher deleted"));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid DELETE path");
        }
    }

    // ===================== HELPER =====================
    private static int parseIdOrThrow(String str, String label) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid " + label + " ID");
        }
    }

    private static String jsonError(String msg) {
        return "{\"error\": \"" + msg.replace("\"", "'") + "\"}";
    }

    private static String jsonMessage(String msg) {
        return "{\"message\": \"" + msg.replace("\"", "'") + "\"}";
    }
}
