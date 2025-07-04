package Http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {

    private final HttpExchange httpExchange;
    private final Headers headers;
    private String rawBody;
    private String jsonBody;

    public Request(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headers = httpExchange.getRequestHeaders();
    }

    public String getBody() {
        if (this.rawBody == null) {
            this.rawBody = new BufferedReader(
                    new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8)
            )
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
        return this.rawBody;
    }

    public String getRequestMethod() {
        return httpExchange.getRequestMethod();
    }

    public String getContentType() {
        return headers.getFirst("Content-Type");
    }

    public Map<String, Object> getJSON() throws JsonProcessingException {
        if (!getContentType().equalsIgnoreCase("application/json")) {
            return null;
        }

        Map<String, Object> jsonMap = new HashMap<>();
        if (jsonBody == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonMap = objectMapper.readValue(this.getBody(), new TypeReference<>(){});
        }

        return jsonMap;
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<>();
        String query = httpExchange.getRequestURI().getQuery(); // ci_date=2025-06-20&co_date=2025-06-25

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    String key = java.net.URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    queryParams.put(key, value);
                }
            }
        }

        return queryParams;
    }

    public String getPath() {
        return "";
    }

    public Object getMethod() {
        return null;
    }
}
