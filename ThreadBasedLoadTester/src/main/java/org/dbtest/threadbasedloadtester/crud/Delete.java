package org.dbtest.threadbasedloadtester.crud;

import org.dbtest.threadbasedloadtester.utils.*;

import java.net.http.HttpResponse;


public class Delete {
    public static CrudUtils crudUtils = new CrudUtils();
    private static final String BASE_URL = "http://localhost:8080/api/products/";
    private static final int PRODUCTS_PER_THREAD = crudUtils.nr; // Hur många produkter varje tråd raderar
    private static int threadCounter = 0;

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * PRODUCTS_PER_THREAD + 1;
    }

    public static void runTest() {
        int startId = getNextThreadStart();
        int endId = startId + PRODUCTS_PER_THREAD - 1;

        for (int i = startId; i <= endId; i++) {
            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL + i, "DELETE", null);
            if (response != null) {
                System.out.println("DELETE response for ID " + i + ": " + response.body());
            } else {
                System.out.println("DELETE request failed for ID " + i);
            }
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
