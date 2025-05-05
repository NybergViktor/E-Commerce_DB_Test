package org.dbtest.threadbasedloadtester.load;

import org.dbtest.threadbasedloadtester.latency.CreateLatency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestsCreateLatency {

    private static final int THREAD_COUNT = 100;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\n Startar CRUD-operationer...");
        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("\n Startar latensmätning...");
            executor.submit(CreateLatency::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sammanställ global statistik efter att alla trådar är klara
        double globalAvgLatency = (double) CreateLatency.globalTotalLatency / CreateLatency.globalRequestCount;

        System.out.println("\n=== Global Create Latency Summary ===");
        System.out.printf("Average Latency: %.2f ms\n", globalAvgLatency);
        System.out.println("Min Latency: " + CreateLatency.globalMinLatency + " ms");
        System.out.println("Max Latency: " + CreateLatency.globalMaxLatency + " ms");

        // Spara sammanfattning till fil
        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (CreateLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/global_create_summary.csv")) {
            writer.write("SUMMARY\n");
            writer.write("Average," + globalAvgLatency + "\n");
            writer.write("Min," + CreateLatency.globalMinLatency + "\n");
            writer.write("Max," + CreateLatency.globalMaxLatency + "\n");

            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "With %d concurrent CREATE operations (%d total products), %s had an average latency of %.2f ms, with a maximum latency of %d ms.\n",
                    THREAD_COUNT,
                    THREAD_COUNT * CreateLatency.PRODUCTS_PER_THREAD,
                    CreateLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB",
                    globalAvgLatency,
                    CreateLatency.globalMaxLatency
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n Load Test Completed!");
    }
}
