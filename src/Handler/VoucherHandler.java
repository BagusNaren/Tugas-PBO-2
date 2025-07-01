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

        } catch (NotFoundException | BadRequestException | MethodNotAllowedException e) {
            res.setBody("{\"error\": \"" + e.getMessage() + "\"}");
            int code;
            if (e instanceof NotFoundException) {
                code = HttpURLConnection.HTTP_NOT_FOUND;
            } else if (e instanceof BadRequestException) {
                code = HttpURLConnection.HTTP_BAD_REQUEST;
            } else {
                code = HttpURLConnection.HTTP_BAD_METHOD;
            }
            res.send(code);
        } catch (Exception e) {
            e.printStackTrace();
            res.setBody("{\"error\": \"" + e.getMessage() + "\"}");
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
            Voucher voucher = getVoucherOrThrow(parts[2]);
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
            validateVoucher(voucher);
            VoucherDAO.insert(voucher);
            res.setBody("{\"message\": \"Voucher added successfully\"}");
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
            validateVoucher(voucher);
            voucher.setId(id);
            VoucherDAO.update(voucher);
            res.setBody("{\"message\": \"Voucher updated successfully\"}");
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    // ===================== DELETE =====================
    private static void handleDelete(String[] parts, Response res) {
        if (parts.length == 3 && parts[1].equals("vouchers")) {
            int id = parseIdOrThrow(parts[2], "voucher");
            Voucher voucher = VoucherDAO.getById(id);
            if (voucher == null) throw new NotFoundException("Voucher not found");
            VoucherDAO.delete(id);
            res.setBody("{\"message\": \"Voucher deleted successfully\"}");
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid DELETE path");
        }
    }

    // ===================== HELPER =====================
    private static int parseIdOrThrow(String value, String label) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid " + label + " ID: must be an integer");
        }
    }

    private static Voucher getVoucherOrThrow(String idStr) {
        int id = parseIdOrThrow(idStr, "voucher");
        Voucher voucher = VoucherDAO.getById(id);
        if (voucher == null) throw new NotFoundException("Voucher not found");
        return voucher;
    }

    private static void validateVoucher(Voucher voucher) {
        if (voucher.getCode() == null || voucher.getCode().isBlank()) {
            throw new BadRequestException("Voucher code is required");
        }
        if (voucher.getDescription() == null || voucher.getDescription().isBlank()) {
            throw new BadRequestException("Voucher description is required");
        }
        if (voucher.getDiscount() <= 0 || voucher.getDiscount() > 1) {
            throw new BadRequestException("Discount must be between 0 and 1");
        }
        if (voucher.getStartDate() == null || voucher.getStartDate().isBlank()) {
            throw new BadRequestException("Start date is required");
        }
        if (voucher.getEndDate() == null || voucher.getEndDate().isBlank()) {
            throw new BadRequestException("End date is required");
        }
    }
}
