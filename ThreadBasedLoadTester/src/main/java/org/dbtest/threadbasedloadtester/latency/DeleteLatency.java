package org.dbtest.threadbasedloadtester.latency;

import org.dbtest.threadbasedloadtester.utils.*;

import java.net.http.HttpResponse;


public class DeleteLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    private static final String BASE_URL = "http://localhost:8080/api/products/";
    private static final int PRODUCTS_PER_THREAD = latencyUtils.nr;
    private static int threadCounter = 0;

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
            long startTime = System.nanoTime();
            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL + i, "DELETE", null);
            long endTime = System.nanoTime();

            long latency = (endTime - startTime) / 1_000_000;
            totalLatency += latency;
            minLatency = Math.min(minLatency, latency);
            maxLatency = Math.max(maxLatency, latency);

            System.out.println("DELETE Request " + i + ": " + latency + " ms");
        }

        double avgLatency = (double) totalLatency / PRODUCTS_PER_THREAD;
        System.out.println("\n DELETE Latency Summary ");
        System.out.println("Average latency: " + avgLatency + " ms | Min: " + minLatency + " ms | Max: " + maxLatency + " ms");
    }

    public static void main(String[] args) {
        runTest();
    }
}
