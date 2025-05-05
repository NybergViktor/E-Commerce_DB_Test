package org.dbtest.threadbasedloadtester.crud;

import com.jayway.jsonpath.internal.Utils;
import org.dbtest.threadbasedloadtester.utils.*;


import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Random;

public class Create {
    public static CrudUtils crudUtils = new CrudUtils();
    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static final int PRODUCTS_PER_THREAD = crudUtils.nr;
    private static int threadCounter = 0;
    static int nr_of_products = 0;

    private static final Random random = new Random();

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * PRODUCTS_PER_THREAD + 1;
    }

    public static void runTest() {
        int startId = getNextThreadStart();
        int endId = startId + PRODUCTS_PER_THREAD - 1;

        for (int i = startId; i <= endId; i++) {
            double price = 100 + (900 * random.nextDouble()); // Pris mellan 100.00 och 1000.00
            int stock = random.nextInt(100) + 1; // Lager mellan 1 och 100

            String jsonPayload = String.format(Locale.US,
                    "{\"name\": \"Product %d\", \"description\": \"Load test item %d\", \"price\": %.2f, \"stock\": %d}",
                    i, i, price, stock
            );

            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "POST", jsonPayload);
            if (response != null) {
                //  System.out.println("CREATE response for ID " + i + ": " + response.body());
            } else {
                System.out.println("CREATE request failed for ID " + i);
            }
            nr_of_products++;
        }
        System.out.println("Created " + nr_of_products + " products");
    }

    public static void main(String[] args) {
        runTest();
    }
}