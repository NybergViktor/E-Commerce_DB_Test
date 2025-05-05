package org.dbtest.threadbasedloadtester.crud;

import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class productConcurrentPutTest {

    private static final int THREAD_COUNT = 100;
    private static final int REQUESTS_PER_THREAD = 100;
    private static final String BASE_URL = "http://localhost:8080/api/products/";
    private static final String TARGET_ID = "681676d85b61af6e8ed702c71";

    // Globala räknare
    private static final AtomicInteger successCount = new AtomicInteger(0);
    private static final AtomicInteger failCount = new AtomicInteger(0);
    private static final LongAdder totalLatency = new LongAdder();
    private static final AtomicInteger maxLatency = new AtomicInteger(0);
    private static final AtomicInteger minLatency = new AtomicInteger(Integer.MAX_VALUE);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\nStartar Concurrent PUT-test...");

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    String jsonPayload = String.format("""
                            {
                                "id": "%s",
                                "name": "Concurrent Updated Product",
                                "description": "Thread test",
                                "price": 199.99,
                                "stock": 30
                            }
                            """, TARGET_ID);

                    long start = System.nanoTime();
                    HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL + TARGET_ID, "PUT", jsonPayload);
                    long end = System.nanoTime();
                    long latency = (end - start) / 1_000_000;

                    totalLatency.add(latency);
                    maxLatency.updateAndGet(prev -> Math.max(prev, (int) latency));
                    minLatency.updateAndGet(prev -> Math.min(prev, (int) latency));

                    if (response != null && response.statusCode() == 200) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int total = successCount.get() + failCount.get();
        double avgLatency = total > 0 ? totalLatency.doubleValue() / total : 0;

        System.out.println("\n=== Sammanställning ===");
        System.out.println("Totalt antal requests: " + total);
        System.out.println("Lyckade requests (200): " + successCount.get());
        System.out.println("Misslyckade requests: " + failCount.get());
        System.out.printf("Genomsnittlig latency: %.2f ms%n", avgLatency);
        System.out.println("Minsta latency: " + minLatency.get() + " ms");
        System.out.println("Största latency: " + maxLatency.get() + " ms");
        System.out.println("Concurrent PUT-test klart.");
    }
}
