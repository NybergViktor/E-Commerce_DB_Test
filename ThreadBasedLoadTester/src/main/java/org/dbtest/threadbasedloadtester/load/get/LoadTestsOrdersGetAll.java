package org.dbtest.threadbasedloadtester.load.get;

import org.dbtest.threadbasedloadtester.latency.get.SearchProductLatency;
import org.dbtest.threadbasedloadtester.latency.get.getOrders;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestsOrdersGetAll {
    private static final int THREAD_COUNT = 1;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\nStartar GET-loadtester...");

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(getOrders::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double globalAvgLatency = (double) getOrders.globalTotalLatency / getOrders.globalRequestCount;

        System.out.println("\n=== Global order GET all Latency Summary ===");
        System.out.printf("Average Latency: %.2f ms\n", globalAvgLatency);
        System.out.println("Min Latency: " + getOrders.globalMinLatency + " ms");
        System.out.println("Max Latency: " + getOrders.globalMaxLatency + " ms");
        System.out.println("Total orders returned across all threads: " + getOrders.globalTotalOrdersReturned);


        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (getOrders.sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/global_get_all_orders_summary.csv")) {
            writer.write("SUMMARY\n");
            writer.write("Average," + globalAvgLatency + "\n");
            writer.write("Min," + getOrders.globalMinLatency + "\n");
            writer.write("Max," + getOrders.globalMaxLatency + "\n");
            writer.write("Total Orders Returned," + getOrders.globalTotalOrdersReturned + "\n");


            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "With %d concurrent GET operations (%d total queries), %s had an average latency of %.2f ms, and a max latency of %d ms.\n",
                    THREAD_COUNT,
                    THREAD_COUNT * getOrders.ORDERS_PER_THREAD,
                    getOrders.sqlOrMongoDb ? "PostgreSQL" : "MongoDB",
                    globalAvgLatency,
                    getOrders.globalMaxLatency
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nLoad Test Completed!");
    }
}
