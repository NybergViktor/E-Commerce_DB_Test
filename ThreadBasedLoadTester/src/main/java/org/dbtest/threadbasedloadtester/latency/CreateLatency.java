package org.dbtest.threadbasedloadtester.latency;

import org.dbtest.threadbasedloadtester.utils.*;

import java.net.http.HttpResponse;

public class CreateLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    public static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;

    private static final String BASE_URL = "http://localhost:8080/api/products";
    public static final int PRODUCTS_PER_THREAD = latencyUtils.nr;
    private static int threadCounter = 0;

    // Global shared metrics
    public static long globalTotalLatency = 0;
    public static long globalMinLatency = Long.MAX_VALUE;
    public static long globalMaxLatency = 0;
    public static int globalRequestCount = 0;

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

        for (int i = startId; i <= endId; i++) {
            String jsonPayload = """
                    {
                        "name": "Latency Test Product %d",
                        "description": "Load test latency",
                        "price": 99.99,
                        "stock": 50
                    }
                    """.formatted(i);

            long startTime = System.nanoTime();
            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "POST", jsonPayload);
            long endTime = System.nanoTime();

            long latency = (endTime - startTime) / 1_000_000;
            totalLatency += latency;
            minLatency = Math.min(minLatency, latency);
            maxLatency = Math.max(maxLatency, latency);
        }

        synchronized (lock) {
            globalTotalLatency += totalLatency;
            globalMinLatency = Math.min(globalMinLatency, minLatency);
            globalMaxLatency = Math.max(globalMaxLatency, maxLatency);
            globalRequestCount += PRODUCTS_PER_THREAD;
        }
    }
}
