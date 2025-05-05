package org.dbtest.threadbasedloadtester.latency.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbtest.threadbasedloadtester.latency.LatencyUtils;
import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;

public class getProducts {
    static LatencyUtils latencyUtils = new LatencyUtils();
    public static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;
    private static final String BASE_URL = "http://localhost:8080/api/products/all";
    public static final int PRODUCTS_PER_THREAD = latencyUtils.nr;
    private static int threadCounter = 0;
    private static final String DATABASE = sqlOrMongoDb ? "PostgreSQL" : "MongoDB";

    // Globala ackumulerade latensvärden
    public static long globalTotalLatency = 0;
    public static long globalMinLatency = Long.MAX_VALUE;
    public static long globalMaxLatency = 0;
    public static int globalRequestCount = 0;
    public static int globalTotalProductsReturned = 0;


    private static final Object lock = new Object();

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * PRODUCTS_PER_THREAD + 1;
    }

    public static void runTest() {
        long totalLatency = 0;
        long minLatency = Long.MAX_VALUE;
        long maxLatency = 0;

        int startId = getNextThreadStart();
        int endId = startId + PRODUCTS_PER_THREAD - 1;
        ObjectMapper mapper = new ObjectMapper();

        for (int i = startId; i <= endId; i++) {
            long startTime = System.nanoTime();
            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "GET", null);
            long endTime = System.nanoTime();

            long latency = (endTime - startTime) / 1_000_000;
            totalLatency += latency;
            minLatency = Math.min(minLatency, latency);
            maxLatency = Math.max(maxLatency, latency);

            if (response != null && response.statusCode() == 200) {
                try {
                    JsonNode json = mapper.readTree(response.body());
                    if (json.isArray()) {
                        int count = json.size();
                        synchronized (lock) {
                            globalTotalProductsReturned += count;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse JSON in GET request #" + i);
                }
            }
        }


        synchronized (lock) {
            globalTotalLatency += totalLatency;
            globalMinLatency = Math.min(globalMinLatency, minLatency);
            globalMaxLatency = Math.max(globalMaxLatency, maxLatency);
            globalRequestCount += PRODUCTS_PER_THREAD;
        }
    }
}
