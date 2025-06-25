package Handler;

import DAO.VoucherDAO;
import Model.Voucher;
import Tugas2.Request;
import Tugas2.Response;
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
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Request req = new Request(exchange);
        Response res = new Response(exchange);
        String[] parts = path.split("/");

        try {
            if (parts.length == 2 && parts[1].equals("vouchers")) {
                if (method.equals("GET")) {
                    handleGetAll(res);
                } else if (method.equals("POST")) {
                    handlePost(req, res);
                } else {
                    throw new MethodNotAllowedException("Method " + method + " not allowed on /vouchers");
                }
            } else if (parts.length == 3 && parts[1].equals("vouchers")) {
                int id = parseIdOrThrow(parts[2], "voucher");
                if (method.equals("GET")) {
                    handleGetById(id, res);
                } else if (method.equals("PUT")) {
                    handlePut(id, req, res);
                } else if (method.equals("DELETE")) {
                    handleDelete(id, res);
                } else {
                    throw new MethodNotAllowedException("Method " + method + " not allowed on /vouchers/{id}");
                }
            } else {
                throw new NotFoundException("Endpoint " + path + " not found");
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

    private static void handleGetAll(Response res) throws IOException {
        List<Voucher> vouchers = VoucherDAO.getAll();
        res.setBody(objectMapper.writeValueAsString(vouchers));
        res.send(HttpURLConnection.HTTP_OK);
    }

    private static void handlePost(Request req, Response res) throws IOException {
        Voucher voucher = objectMapper.readValue(req.getBody(), Voucher.class);
        if (voucher.getCode() == null || voucher.getCode().isEmpty()) {
            throw new BadRequestException("Voucher code cannot be empty");
        }
        VoucherDAO.insert(voucher);
        res.setBody(objectMapper.writeValueAsString(voucher));
        res.send(HttpURLConnection.HTTP_CREATED);
    }

    private static void handleGetById(int id, Response res) throws IOException {
        Voucher voucher = VoucherDAO.getById(id);
        if (voucher == null) {
            throw new NotFoundException("Voucher with ID " + id + " not found");
        }
        res.setBody(objectMapper.writeValueAsString(voucher));
        res.send(HttpURLConnection.HTTP_OK);
    }

    private static void handlePut(int id, Request req, Response res) throws IOException {
        Voucher voucher = objectMapper.readValue(req.getBody(), Voucher.class);
        voucher.setId(id);
        if (voucher.getCode() == null || voucher.getCode().isEmpty()) {
            throw new BadRequestException("Voucher code cannot be empty");
        }
        VoucherDAO.update(voucher);
        res.setBody(objectMapper.writeValueAsString(voucher));
        res.send(HttpURLConnection.HTTP_OK);
    }

    private static void handleDelete(int id, Response res) throws IOException {
        VoucherDAO.delete(id);
        res.setBody(jsonMessage("Voucher deleted"));
        res.send(HttpURLConnection.HTTP_OK);
    }

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
