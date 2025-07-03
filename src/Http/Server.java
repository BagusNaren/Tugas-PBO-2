package Http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import Handler.VillaHandler;
import Handler.CustomerHandler;
import Handler.VoucherHandler;
import Exception.NotFoundException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final HttpServer server;

    private static class RequestHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            try {
                Server.processHttpExchange(httpExchange);
            } catch (NotFoundException e) {
                sendErrorResponse(httpExchange, 404, e.getMessage());
            } catch (Exception e) {
                sendErrorResponse(httpExchange, 500, "Internal Server Error");
                e.printStackTrace();
            }
        }
    }

    public Server(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 128);

        // Endpoint utama
        server.createContext("/villas", VillaHandler::handle);
        server.createContext("/villas/", VillaHandler::handle);
        server.createContext("/customers", CustomerHandler::handle);
        server.createContext("/customers/", CustomerHandler::handle);
        server.createContext("/vouchers", VoucherHandler::handle);
        server.createContext("/vouchers/", VoucherHandler::handle);

        // Ping test
        server.createContext("/ping", exchange -> {
            String response = "{\"message\": \"pong\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        // Default handler
        server.createContext("/", new RequestHandler());

        server.start();
    }

    public static void processHttpExchange(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String method = httpExchange.getRequestMethod();

        // Validasi endpoint tidak dikenali → lempar NotFoundException
        if (!(path.startsWith("/villas") ||
                path.startsWith("/customers") ||
                path.startsWith("/vouchers") ||
                path.equals("/ping") ||
                path.equals("/"))) {
            throw new NotFoundException("Endpoint " + path + " not found");
        }

        // Logging tambahan
        System.out.println("========== Incoming Request ==========");
        System.out.println("Method: " + method);
        System.out.println("Path: " + path);
        System.out.println("Headers: " + httpExchange.getRequestHeaders());
        try {
            String body = req.getBody();
            System.out.println("Body:");
            System.out.println(body);
        } catch (Exception e) {
            System.out.println("Body could not be read: " + e.getMessage());
        }

        // Handler default jika belum dikirim
        if (res.isSent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resJsonMap = new HashMap<>();
            resJsonMap.put("message", "Request Success (Default Handler)");

            String resJson = "";
            try {
                resJson = objectMapper.writeValueAsString(resJsonMap);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            res.setBody(resJson);
            res.send(HttpURLConnection.HTTP_OK);
        }
    }

    private static void sendErrorResponse(HttpExchange exchange, int statusCode, String message) {
        try {
            Map<String, Object> error = new HashMap<>();
            error.put("error", message);
            String json = new ObjectMapper().writeValueAsString(error);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, json.getBytes().length);
            exchange.getResponseBody().write(json.getBytes());
        } catch (Exception e) {
            System.out.println("Gagal kirim response error: " + e.getMessage());
        } finally {
            try {
                exchange.getResponseBody().close();
            } catch (Exception ignored) {}
        }
    }
}
