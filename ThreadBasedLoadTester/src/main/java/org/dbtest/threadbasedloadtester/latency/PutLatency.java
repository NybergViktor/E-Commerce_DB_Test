package org.dbtest.threadbasedloadtester.latency;

import org.dbtest.threadbasedloadtester.utils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

public class PutLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    private static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;

    private static boolean isSQLOrMongoDb() {
        return sqlOrMongoDb;
    }

    private static final String BASE_URL = "http://localhost:8080/api/products/";
    private static final int PRODUCTS_PER_THREAD = latencyUtils.nr;
    private static final int THREAD_COUNT = 1;
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

        try (FileWriter writer = new FileWriter(folderPath + "/put_results_" + (isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB") + ".csv")) {
            writer.write("Request,Latency(ms)\n");

            for (int i = startId; i <= endId; i++) {
                String jsonPayload = """
                        {
                            "id": "%d",
                            "name": "Updated Latency Product %d",
                            "description": "Updated latency description",
                            "price": 199.99,
                            "stock": 30
                        }
                        """.formatted(i, i);

                long startTime = System.nanoTime();
                HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL + i, "PUT", jsonPayload);
                long endTime = System.nanoTime();

                long latency = (endTime - startTime) / 1_000_000;
                totalLatency += latency;
                minLatency = Math.min(minLatency, latency);
                maxLatency = Math.max(maxLatency, latency);
            }

            double avgLatency = (double) totalLatency / PRODUCTS_PER_THREAD;

            writer.write("\nSUMMARY\n");
            writer.write("Average," + avgLatency + "\n");
            writer.write("Min," + minLatency + "\n");
            writer.write("Max," + maxLatency + "\n");

            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "When we increased to %d concurrent PUT requests (%d total updates), we observed that %s had an average latency of %.2f ms, with a maximum latency of %d ms.\n",
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
