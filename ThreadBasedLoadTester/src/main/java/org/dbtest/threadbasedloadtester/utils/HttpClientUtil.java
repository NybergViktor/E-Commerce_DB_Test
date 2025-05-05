package org.dbtest.threadbasedloadtester.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> sendRequest(String url, String method, String jsonBody) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            switch (method.toUpperCase()) {
                case "POST" -> {
                    requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
                    requestBuilder.header("Content-Type", "application/json");
                }
                case "PUT" -> {
                    requestBuilder.PUT(
                            jsonBody != null ? HttpRequest.BodyPublishers.ofString(jsonBody) : HttpRequest.BodyPublishers.noBody()
                    );
                    requestBuilder.header("Content-Type", "application/json");
                }
                case "DELETE" -> requestBuilder.DELETE();
                case "GET" -> requestBuilder.GET();
                default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }

            return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }

}
