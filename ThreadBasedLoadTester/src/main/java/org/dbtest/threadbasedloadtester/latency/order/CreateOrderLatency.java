package org.dbtest.threadbasedloadtester.latency.order;

import org.dbtest.threadbasedloadtester.latency.LatencyUtils;
import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

public class CreateOrderLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    private static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;
    private static final String BASE_URL = "http://localhost:8080/api/orders"; // Adjusted to plural 'orders'
    private static final int ORDERS_PER_THREAD = latencyUtils.nr; // Number of orders each thread will create
    private static int threadCounter = 1; // Used to assign each thread a unique starting ID
    private static final String DATABASE = sqlOrMongoDb ? "PostgreSQL" : "MongoDB";

    // Synchronized method to get the next starting ID for a thread
    public static synchronized int getNextThreadStart() {
        return threadCounter++ * ORDERS_PER_THREAD + 1;
    }

    public static void runTest() {
        long totalLatency = 0;
        long minLatency = Long.MAX_VALUE;
        long maxLatency = 0;

        int startId = getNextThreadStart();
        int endId = startId + ORDERS_PER_THREAD - 1;

        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/create_order_latency_results_" + (sqlOrMongoDb ? "PostgreSQL" : "MongoDB") + ".csv")) {
            writer.write("RequestID,Latency(ms)\n");

            for (int i = startId; i <= endId; i++) {

                // Building the JSON payload
                String jsonPayload = """
                        {
                            "products": [
                                "7",
                                "8"
                            ],
                            "buyer_id": "3"
                        }
                        """;

                long startTime = System.nanoTime();
                HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "POST", jsonPayload);
                long endTime = System.nanoTime();

                long latency = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
                totalLatency += latency;
                minLatency = Math.min(minLatency, latency);
                maxLatency = Math.max(maxLatency, latency);

                if (response != null) {
                    System.out.println("CREATE response for ID " + i + ": " + response.body());
                } else {
                    System.out.println("CREATE request failed for ID " + i);
                }

                // Write latency for each request to CSV
                writer.write(i + "," + latency + "\n");
            }

            double avgLatency = (double) totalLatency / ORDERS_PER_THREAD;

            // Console summary
            System.out.println("\n Create Order Latency Summary ");
            System.out.printf("Average Latency: %.2f ms\n", avgLatency);
            System.out.println("Min Latency: " + minLatency + " ms");
            System.out.println("Max Latency: " + maxLatency + " ms");

            // Write summary at the bottom of CSV
            writer.write("\nSUMMARY\n");
            writer.write("Average," + avgLatency + "\n");
            writer.write("Min," + minLatency + "\n");
            writer.write("Max," + maxLatency + "\n");

            // Write automatic commentary
            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "During the test of creating %d orders, the database %s achieved an average latency of %.2f ms, with a minimum of %d ms and a maximum of %d ms.\n",
                    ORDERS_PER_THREAD, DATABASE, avgLatency, minLatency, maxLatency
            ));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
