package org.dbtest.threadbasedloadtester.load;

import org.dbtest.threadbasedloadtester.latency.CreateLatency;
import org.dbtest.threadbasedloadtester.latency.PutLatency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadTestsPutLatency {

    private static final int THREAD_COUNT = 20;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("\n Startar CRUD-operationer...");
        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("\n Startar latensmätning...");
            executor.submit(PutLatency::runTest);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n Load Test Completed!");
    }

}