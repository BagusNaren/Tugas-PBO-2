package Handler;

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

public class CustomerHandler {
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
            } else if (e instanceof MethodNotAllowedException) {
                code = HttpURLConnection.HTTP_BAD_METHOD;
            } else {
                code = HttpURLConnection.HTTP_INTERNAL_ERROR;
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
        if (parts.length == 2) {
            List<Customer> customers = CustomerDAO.getAll();
            res.setBody(objectMapper.writeValueAsString(customers));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 3) {
            Customer customer = getCustomerOrThrow(parts[2]);
            res.setBody(objectMapper.writeValueAsString(customer));
            res.send(HttpURLConnection.HTTP_OK);
        } else if (parts.length == 4) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            if (parts[3].equals("bookings")) {
                List<Booking> bookings = BookingDAO.getAllByCustomer(customerId);
                res.setBody(objectMapper.writeValueAsString(bookings));
                res.send(HttpURLConnection.HTTP_OK);
            } else if (parts[3].equals("reviews")) {
                List<Review> reviews = ReviewDAO.getAllByCustomerId(customerId);
                res.setBody(objectMapper.writeValueAsString(reviews));
                res.send(HttpURLConnection.HTTP_OK);
            } else {
                throw new NotFoundException("Invalid GET path");
            }
        } else {
            throw new NotFoundException("Invalid GET path");
        }
    }

    // ===================== POST =====================
    private static void handlePost(String[] parts, Request req, Response res) throws IOException {
        String body = req.getBody();
        if (parts.length == 2) {
            Customer customer = objectMapper.readValue(body, Customer.class);
            validateCustomer(customer);
            CustomerDAO.insert(customer);
            res.setBody("{\"message\": \"Customer added successfully\"}");
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 4 && parts[3].equals("bookings")) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Booking booking = objectMapper.readValue(body, Booking.class);
            booking.setCustomerId(customerId);
            BookingDAO.insert(booking);
            res.setBody("{\"message\": \"Booking added successfully\"}");
            res.send(HttpURLConnection.HTTP_CREATED);
        } else if (parts.length == 6 && parts[3].equals("bookings") && parts[5].equals("reviews")) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            int bookingId = parseIdOrThrow(parts[4], "booking");
            Review review = objectMapper.readValue(body, Review.class);
            review.setCustomerId(customerId);
            review.setBookingId(bookingId);
            ReviewDAO.insert(review);
            res.setBody("{\"message\": \"Review added successfully\"}");
            res.send(HttpURLConnection.HTTP_CREATED);
        } else {
            throw new NotFoundException("Invalid POST path");
        }
    }

    // ===================== PUT =====================
    private static void handlePut(String[] parts, Request req, Response res) throws IOException {
        if (parts.length == 3) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer customer = objectMapper.readValue(req.getBody(), Customer.class);
            validateCustomer(customer);
            customer.setId(customerId);
            CustomerDAO.update(customer);
            res.setBody("{\"message\": \"Customer updated successfully\"}");
            res.send(HttpURLConnection.HTTP_OK);
        } else {
            throw new NotFoundException("Invalid PUT path");
        }
    }

    // ===================== DELETE =====================
    private static void handleDelete(String[] parts, Response res) {
        if (parts.length == 3) {
            int customerId = parseIdOrThrow(parts[2], "customer");
            Customer customer = CustomerDAO.getById(customerId);
            if (customer == null) throw new NotFoundException("Customer not found");
            CustomerDAO.delete(customerId);
            res.setBody("{\"message\": \"Customer deleted successfully\"}");
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

    private static Customer getCustomerOrThrow(String idStr) {
        int id = parseIdOrThrow(idStr, "customer");
        Customer customer = CustomerDAO.getById(id);
        if (customer == null) throw new NotFoundException("Customer not found");
        return customer;
    }

    private static void validateCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new BadRequestException("Customer name is required");
        }
    }
}