package org.dbtest.threadbasedloadtester.crud.order;

import org.dbtest.threadbasedloadtester.crud.CrudUtils;
import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;

public class CreateOrder {
    public static CrudUtils crudUtils = new CrudUtils();
    private static final String BASE_URL = "http://localhost:8080/api/orders";
    private static final int ORDERS_PER_THREAD = crudUtils.nr; // How many orders each thread will create
    private static int threadCounter = 0; // Used to assign each thread a unique starting ID

    // Synchronized method to get the next starting ID for a thread
    public static synchronized int getNextThreadStart() {
        return threadCounter++ * ORDERS_PER_THREAD + 1;
    }

    public static void runTest() {
        int startId = getNextThreadStart();
        int endId = startId + ORDERS_PER_THREAD - 1;

        for (int i = startId; i <= endId; i++) {

            // Building a complete JSON payload
            String jsonPayload = """
                    {
                        "products": [
                        "5",
                        "6"
                    ],
                        "buyer_id": "1"
                    }
                    """;

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
