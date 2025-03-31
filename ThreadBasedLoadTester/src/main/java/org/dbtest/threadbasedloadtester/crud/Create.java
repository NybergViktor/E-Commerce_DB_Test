package org.dbtest.threadbasedloadtester.crud;

import com.jayway.jsonpath.internal.Utils;
import org.dbtest.threadbasedloadtester.utils.*;


import java.net.http.HttpResponse;

public class Create {
    public static CrudUtils crudUtils = new CrudUtils();
    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static final int PRODUCTS_PER_THREAD = crudUtils.nr; // Hur många produkter varje tråd skapar
    private static int threadCounter = 0; // För att ge varje tråd ett unikt start-ID

    public static synchronized int getNextThreadStart() {
        return threadCounter++ * PRODUCTS_PER_THREAD + 1;
    }

    public static void runTest() {
        int startId = getNextThreadStart();
        int endId = startId + PRODUCTS_PER_THREAD - 1;

        for (int i = startId; i <= endId; i++) {
            String jsonPayload = """
                    {
                        "name": "Product %d",
                        "description": "Load test item",
                        "price": 99.99,
                        "stock": 50
                    }
                    """.formatted(i, i);

            HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "POST", jsonPayload);
            if (response != null) {
                System.out.println("CREATE response for ID " + i + ": " + response.body());
            } else {
                System.out.println("CREATE request failed for ID " + i);
            }
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}