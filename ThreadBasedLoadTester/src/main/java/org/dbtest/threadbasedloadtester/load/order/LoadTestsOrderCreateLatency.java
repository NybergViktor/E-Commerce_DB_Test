package org.dbtest.threadbasedloadtester.load.order;

import org.dbtest.threadbasedloadtester.latency.order.CreateOrderLatency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestsOrderCreateLatency {
    private static final int THREAD_COUNT = 1;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\n Startar CRUD-operationer...");
        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("\n Startar latensmätning...");
            executor.submit(CreateOrderLatency::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sammanställning av global latens
        double globalAvgLatency = (double) CreateOrderLatency.globalTotalLatency / CreateOrderLatency.globalRequestCount;

        System.out.println("\n=== Global Create Order Latency Summary ===");
        System.out.printf("Average Latency: %.2f ms\n", globalAvgLatency);
        System.out.println("Min Latency: " + CreateOrderLatency.globalMinLatency + " ms");
        System.out.println("Max Latency: " + CreateOrderLatency.globalMaxLatency + " ms");

        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (CreateOrderLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/global_create_order_summary.csv")) {
            writer.write("SUMMARY\n");
            writer.write("Average," + globalAvgLatency + "\n");
            writer.write("Min," + CreateOrderLatency.globalMinLatency + "\n");
            writer.write("Max," + CreateOrderLatency.globalMaxLatency + "\n");

            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "With %d concurrent CREATE ORDER operations (%d total orders), %s had an average latency of %.2f ms, with a maximum latency of %d ms.\n",
                    THREAD_COUNT,
                    THREAD_COUNT * CreateOrderLatency.ORDERS_PER_THREAD,
                    CreateOrderLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB",
                    globalAvgLatency,
                    CreateOrderLatency.globalMaxLatency
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n Load Test Completed!");
    }
}
