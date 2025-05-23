package org.dbtest.threadbasedloadtester.latency.order;

import org.dbtest.threadbasedloadtester.latency.LatencyUtils;
import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;

public class CreateOrderLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    public static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;
    private static final String BASE_URL = "http://localhost:8080/api/order";
    public static final int ORDERS_PER_THREAD = latencyUtils.nr;
    private static int threadCounter = 0;
    private static final String DATABASE = sqlOrMongoDb ? "PostgreSQL" : "MongoDB";

    // Globala ackumulerade latensvärden
    public static long globalTotalLatency = 0;
    public static long globalMinLatency = Long.MAX_VALUE;
    public static long globalMaxLatency = 0;
    public static int globalRequestCount = 0;

    private static final Object lock = new Object();

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * ORDERS_PER_THREAD + 1;
    }

    public static void runTest() {
        long totalLatency = 0;
        long minLatency = Long.MAX_VALUE;
        long maxLatency = 0;

        int startId = getNextThreadStart();
        int endId = startId + ORDERS_PER_THREAD - 1;
        for (int i = startId; i <= endId; i++) {
            String jsonPayload = String.format("""
                    {
                        "products": [
                            "681676d85b61af6e8ed702c7%d",
                            "681676d85b61af6e8ed702c8%d"
                        ],
                        "buyer_id": "6816763f5b61af6e8ed702c6"
                    }
                    """, i, i);

            long startTime = System.nanoTime();
            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "POST", jsonPayload);
            long endTime = System.nanoTime();

            long latency = (endTime - startTime) / 1_000_000;
            totalLatency += latency;
            minLatency = Math.min(minLatency, latency);
            maxLatency = Math.max(maxLatency, latency);

            if (response != null) {
                // System.out.println("CREATE response for ID " + i + ": " + response.body());
            } else {
                System.out.println("CREATE request failed for ID " + i);
            }
        }

        synchronized (lock) {
            globalTotalLatency += totalLatency;
            globalMinLatency = Math.min(globalMinLatency, minLatency);
            globalMaxLatency = Math.max(globalMaxLatency, maxLatency);
            globalRequestCount += ORDERS_PER_THREAD;
        }
    }
}
