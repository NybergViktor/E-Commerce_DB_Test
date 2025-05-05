package org.dbtest.threadbasedloadtester.load.get;

import org.dbtest.threadbasedloadtester.latency.get.getProducts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestProductsGetAll {
    private static final int THREAD_COUNT = 1;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\nStartar GET-loadtester...");

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(getProducts::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double globalAvgLatency = (double) getProducts.globalTotalLatency / getProducts.globalRequestCount;

        System.out.println("\n=== Global product GET all Latency Summary ===");
        System.out.printf("Average Latency: %.2f ms\n", globalAvgLatency);
        System.out.println("Min Latency: " + getProducts.globalMinLatency + " ms");
        System.out.println("Max Latency: " + getProducts.globalMaxLatency + " ms");
        System.out.println("Total product returned across all threads: " + getProducts.globalTotalProductsReturned);


        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (getProducts.sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/global_get_all_products_summary.csv")) {
            writer.write("SUMMARY\n");
            writer.write("Average," + globalAvgLatency + "\n");
            writer.write("Min," + getProducts.globalMinLatency + "\n");
            writer.write("Max," + getProducts.globalMaxLatency + "\n");
            writer.write("Total product Returned," + getProducts.globalTotalProductsReturned + "\n");


            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "With %d concurrent GET operations (%d total queries), %s had an average latency of %.2f ms, and a max latency of %d ms.\n",
                    THREAD_COUNT,
                    THREAD_COUNT * getProducts.PRODUCTS_PER_THREAD,
                    getProducts.sqlOrMongoDb ? "PostgreSQL" : "MongoDB",
                    globalAvgLatency,
                    getProducts.globalMaxLatency
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nLoad Test Completed!");
    }
}
