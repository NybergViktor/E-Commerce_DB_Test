package org.dbtest.threadbasedloadtester.latency;

import org.dbtest.threadbasedloadtester.utils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

public class GetLatency {
    static LatencyUtils latencyUtils = new LatencyUtils();
    private static boolean sqlOrMongoDb = latencyUtils.sqlOrMongo;

    private static boolean isSQLOrMongoDb() {
        return sqlOrMongoDb;
    }

    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static final int REQUEST_COUNT = 10;
    private static final int THREAD_COUNT = 1;
    private static final String DATABASE = isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB";

    public static void runTest() {
        long totalLatency = 0;
        long minLatency = Long.MAX_VALUE;
        long maxLatency = 0;

        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/get_results_" + (isSQLOrMongoDb() ? "PostgreSQL" : "MongoDB") + ".csv")) {
            writer.write("Request,Latency(ms)\n");

            for (int i = 1; i <= REQUEST_COUNT; i++) {
                long startTime = System.nanoTime();
                HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "GET", null);
                long endTime = System.nanoTime();

                long latency = (endTime - startTime) / 1_000_000;
                totalLatency += latency;
                minLatency = Math.min(minLatency, latency);
                maxLatency = Math.max(maxLatency, latency);
            }

            double avgLatency = (double) totalLatency / REQUEST_COUNT;

            writer.write("\nSUMMARY\n");
            writer.write("Average," + avgLatency + "\n");
            writer.write("Min," + minLatency + "\n");
            writer.write("Max," + maxLatency + "\n");

            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "When we increased to %d concurrent GET requests (%d total reads), we observed that %s had an average latency of %.2f ms, with a maximum latency of %d ms.\n",
                    THREAD_COUNT, REQUEST_COUNT, DATABASE, avgLatency, maxLatency
            ));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
