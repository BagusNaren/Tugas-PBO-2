package Http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Response {

    private final HttpExchange httpExchange;
    private Headers headers;
    private final StringBuilder stringBuilder;
    private boolean isSent;

    public Response(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.stringBuilder = new StringBuilder();
        this.isSent = false;
    }

    public void setBody(String string) {
        stringBuilder.setLength(0);
        stringBuilder.append(string);
    }

    public void send(int status) {
        try {
            this.httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");

            String body = stringBuilder.toString();
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            this.httpExchange.sendResponseHeaders(status, bodyBytes.length);
            this.httpExchange.getResponseBody().write(bodyBytes);
            this.httpExchange.getResponseBody().flush();
            this.httpExchange.getResponseBody().close();
        } catch (IOException ioe) {
            System.err.println("Problem encountered when sending response.");
            ioe.printStackTrace();
        }
        this.isSent = true;
    }


    public boolean isSent() {
        if (this.httpExchange.getResponseCode() != -1)
            this.isSent = true;
        return !isSent;
    }
}
