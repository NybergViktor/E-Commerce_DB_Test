package org.dbtest.threadbasedloadtester.load.get;


import org.dbtest.threadbasedloadtester.latency.get.SearchProductLatency;
import org.dbtest.threadbasedloadtester.latency.get.getProducts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestsProductSearchLatency {
    private static final int THREAD_COUNT = 5;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);


        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("\nStartar SEARCH-loadtester...");
            executor.submit(SearchProductLatency::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double globalAvgLatency = (double) SearchProductLatency.globalTotalLatency / SearchProductLatency.globalRequestCount;

        System.out.println("\n=== Global Product Search Latency Summary ===");
        System.out.printf("Average Latency: %.2f ms\n", globalAvgLatency);
        System.out.println("Min Latency: " + SearchProductLatency.globalMinLatency + " ms");
        System.out.println("Max Latency: " + SearchProductLatency.globalMaxLatency + " ms");
        System.out.println("Total product returned across all threads: " + SearchProductLatency.globalTotalProductsReturned);
        //  System.out.println("=== Products returned ===");
        //  SearchProductLatency.products.forEach(System.out::println);


        String userHome = System.getProperty("user.home");
        String folderPath = userHome + "/Desktop/sql_result_" + (SearchProductLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB");
        new java.io.File(folderPath).mkdirs();

        try (FileWriter writer = new FileWriter(folderPath + "/global_search_product_summary.csv")) {
            writer.write("SUMMARY\n");
            writer.write("Average," + globalAvgLatency + "\n");
            writer.write("Min," + SearchProductLatency.globalMinLatency + "\n");
            writer.write("Max," + SearchProductLatency.globalMaxLatency + "\n");
            writer.write("Total product Returned," + SearchProductLatency.globalTotalProductsReturned + "\n");
            for (String p : SearchProductLatency.products) {
                writer.write(p + "\n");
            }

            writer.write("\nCOMMENTARY\n");
            writer.write(String.format(
                    "With %d concurrent SEARCH operations (%d total queries), %s had an average latency of %.2f ms, and a max latency of %d ms.\n",
                    THREAD_COUNT,
                    THREAD_COUNT * SearchProductLatency.REQUESTS_PER_THREAD,
                    SearchProductLatency.sqlOrMongoDb ? "PostgreSQL" : "MongoDB",
                    globalAvgLatency,
                    SearchProductLatency.globalMaxLatency
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nLoad Test Completed!");
    }
}
