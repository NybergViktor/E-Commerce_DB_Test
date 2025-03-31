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

            switch (method) {
                case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
                case "PUT" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
                case "DELETE" -> requestBuilder.DELETE();
                default -> requestBuilder.GET();
            }

            requestBuilder.header("Content-Type", "application/json");
            return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}
