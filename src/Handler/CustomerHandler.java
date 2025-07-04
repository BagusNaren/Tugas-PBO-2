package Handler;

import MainApp.Main;
import DAO.BookingDAO;
import DAO.CustomerDAO;
import DAO.ReviewDAO;
import Model.Booking;
import Model.Customer;
import Model.Review;
import Http.Request;
import Http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import Exception.NotFoundException;
import Exception.MethodNotAllowedException;
import Exception.BadRequestException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.LinkedHashMap;

public class CustomerHandler {
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
            res.setBody(jsonError("Internal server error: " + e.getMessage()));
            res.send(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }

    // ========== GET ==========
    private static void handleGet(String[] parts, Response res) throws IOException {
        if (parts.length == 2) {
            List<Customer> customers = CustomerDAO.getAll();
            res.setBody(jsonResponse("Customers retrieved successfully", customers));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 3) {
            Customer customer = getCustomerOrThrow(parts[2]);
            res.setBody(jsonResponse("Customer retrieved successfully", customer));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 4) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer customer = CustomerDAO.getById(customerId);
            if (customer == null) throw new NotFoundException("Customer with ID " + customerId + " not found");

            if ("bookings".equals(parts[3])) {
                List<Booking> bookings = BookingDAO.getAllByCustomer(customerId);
                res.setBody(jsonResponse("Bookings retrieved successfully", bookings));
                res.send(HttpURLConnection.HTTP_OK);
            } else if ("reviews".equals(parts[3])) {
                List<Review> reviews = ReviewDAO.getAllByCustomerId(customerId);
                res.setBody(jsonResponse("Reviews retrieved successfully", reviews));
                res.send(HttpURLConnection.HTTP_OK);
            } else {
                throw new NotFoundException("Invalid GET path");
            }
        } else {
            throw new NotFoundException("Invalid GET path");
        }
    }

    // ========== POST ==========
    private static void handlePost(String[] parts, Request req, Response res) throws IOException {
        String body = req.getBody();
        if (parts.length == 2) {
            Customer customer = objectMapper.readValue(body, Customer.class);
            validateCustomer(customer);
            CustomerDAO.insert(customer);
            res.setBody(jsonResponse("Customer added successfully", customer));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 4 && "bookings".equals(parts[3])) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer customer = CustomerDAO.getById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found");
            Booking booking = objectMapper.readValue(body, Booking.class);
            booking.setCustomerId(customerId);
            BookingDAO.insert(booking);
            res.setBody(jsonResponse("Booking added successfully", booking));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 6 && "bookings".equals(parts[3]) && "reviews".equals(parts[5])) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            int bookingId = parseIdOrThrow(parts[4], "booking");
            Customer customer = CustomerDAO.getById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found");
            Review review = objectMapper.readValue(body, Review.class);
            review.setCustomerId(customerId);
            review.setBookingId(bookingId);
            ReviewDAO.insert(review);
            res.setBody(jsonResponse("Review added successfully", review));
            res.send(HttpURLConnection.HTTP_CREATED);
        } else {
            throw new NotFoundException("Invalid POST path");
        }
    }

    // ========== PUT ==========
    private static void handlePut(String[] parts, Request req, Response res) throws IOException {
        if (parts.length == 3) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer existingCustomer = CustomerDAO.getById(customerId);
            if (existingCustomer == null) {
                throw new NotFoundException("Customer not found");
            }
            Customer customer = objectMapper.readValue(req.getBody(), Customer.class);
            validateCustomer(customer);
            customer.setId(customerId);
            CustomerDAO.update(customer);
            res.setBody(jsonResponse("Customer updated successfully", customer));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    // ========== DELETE ==========
    private static void handleDelete(String[] parts, Response res) throws IOException {
        if (parts.length == 3) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer customer = CustomerDAO.getById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found");
            CustomerDAO.delete(customerId);
            res.setBody(jsonResponse("Customer deleted successfully", customer));
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid DELETE path");
        }
    }

    // ========== HELPERS ==========
    private static int parseIdOrThrow(String value, String label) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid " + label + " ID: must be an integer");
        }
    }

    private static Customer getCustomerOrThrow(String idStr) {
        int id = parseIdOrThrow(idStr, "customer");
        Customer customer = CustomerDAO.getById(id);
        if (customer == null) throw new NotFoundException("Customer not found");
        return customer;
    }

    private static void validateCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new BadRequestException("Customer name is required");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Customer email is required");
        }
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new BadRequestException("Customer phone number is required");
        }
    }

    private static String jsonResponse(String message, Object data) throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("message", message);
        result.put("data", data);
        return objectMapper.writeValueAsString(result);
    }

    private static String jsonError(String message) throws IOException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("error", message);
        return objectMapper.writeValueAsString(result);
    }
}
