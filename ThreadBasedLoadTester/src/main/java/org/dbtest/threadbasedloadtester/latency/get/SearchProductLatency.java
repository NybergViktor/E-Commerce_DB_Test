package org.dbtest.threadbasedloadtester.latency.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbtest.threadbasedloadtester.latency.LatencyUtils;
import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchProductLatency {
    private static final java.util.Random random = new java.util.Random();

    static LatencyUtils latencyUtils = new LatencyUtils();
    public static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;
    public static final int REQUESTS_PER_THREAD = latencyUtils.nr;

    private static int threadCounter = 0;
    private static final Object lock = new Object();
    private static final String DATABASE = sqlOrMongoDb ? "PostgreSQL" : "MongoDB";
    private static final String BASE_URL = "http://localhost:8080/api/products/search";
    static String name = "";
    public static long globalTotalLatency = 0;
    public static long globalMinLatency = Long.MAX_VALUE;
    public static long globalMaxLatency = 0;
    public static int globalRequestCount = 0;
    public static int globalTotalProductsReturned = 0;
    public static List<String> products = new ArrayList<>();

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * REQUESTS_PER_THREAD + 1;
    }

    public static void runTest() {
        long totalLatency = 0;
        long minLatency = Long.MAX_VALUE;
        long maxLatency = 0;

        int startId = getNextThreadStart();
        int endId = startId + REQUESTS_PER_THREAD - 1;
        ObjectMapper mapper = new ObjectMapper();

        for (int i = startId; i <= endId; i++) {
            double minPrice = 0 + (400 * random.nextDouble());     // 0–400
            double maxPrice = 600 + (400 * random.nextDouble());   // 600–1000

            String sortBy = "price";
            boolean ascending = false;

            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append(String.format(Locale.US, "?minPrice=%.2f&maxPrice=%.2f&sortBy=%s&ascending=%b",
                    minPrice, maxPrice,
                    URLEncoder.encode(sortBy, StandardCharsets.UTF_8),
                    ascending));

            if (name != null && !name.isBlank()) {
                urlBuilder.append("&name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            }

            String fullUrl = urlBuilder.toString();

            long startTime = System.nanoTime();
            HttpResponse<String> response = HttpClientUtil.sendRequest(fullUrl, "GET", null);

            long endTime = System.nanoTime();

            long latency = (endTime - startTime) / 1_000_000;
            totalLatency += latency;
            minLatency = Math.min(minLatency, latency);
            maxLatency = Math.max(maxLatency, latency);

            if (response != null && response.statusCode() == 200) {
                try {
                    JsonNode json = mapper.readTree(response.body());
                    products.add(json.asText());
                    if (json.isArray()) {
                        synchronized (lock) {
                            globalTotalProductsReturned += json.size();
                        }
                        for (JsonNode node : json) {
                            synchronized (products) {
                                products.add(node.toString());
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Failed to parse JSON in GET request #" + i);
                }
            } else {
                System.out.printf("SEARCH request failed for ID %d with status %s\n", i,
                        response != null ? response.statusCode() : "NO RESPONSE");
            }
        }

        synchronized (lock) {
            globalTotalLatency += totalLatency;
            globalMinLatency = Math.min(globalMinLatency, minLatency);
            globalMaxLatency = Math.max(globalMaxLatency, maxLatency);
            globalRequestCount += REQUESTS_PER_THREAD;
        }
    }
}
