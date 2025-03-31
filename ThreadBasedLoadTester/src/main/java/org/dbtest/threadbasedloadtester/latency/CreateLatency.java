package org.dbtest.threadbasedloadtester.latency;

import org.dbtest.threadbasedloadtester.utils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

public class CreateLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    private static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;

    private static boolean isSQLOrMongoDb() {
        return sqlOrMongoDb;
    }

    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static final int PRODUCTS_PER_THREAD = latencyUtils.nr;
    private static final int THREAD_COUNT = 1; // Justera om du kör parallellt
    private static final String DATABASE = isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB";
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

        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/create_results_" + (isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB") + ".csv")) {
            writer.write("Request,Latency(ms)\n");

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

            double avgLatency = (double) totalLatency / PRODUCTS_PER_THREAD;

            // Skriv summering i konsol
            System.out.println("\n Create Latency Summary ");

            // Lägg till summering längst ner i CSV
            writer.write("\nSUMMARY\n");
            writer.write("Average," + avgLatency + "\n");
            writer.write("Min," + minLatency + "\n");
            writer.write("Max," + maxLatency + "\n");

            // Automatisk kommentar till presentation
            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "When we increased to %d concurrent CREATE operations (%d total products), we observed that %s had an average latency of %.2f ms, with a maximum latency of %d ms.\n",
                    THREAD_COUNT, THREAD_COUNT * PRODUCTS_PER_THREAD, DATABASE, avgLatency, maxLatency
            ));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
