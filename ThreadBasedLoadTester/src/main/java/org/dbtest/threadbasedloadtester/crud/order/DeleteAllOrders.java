package org.dbtest.threadbasedloadtester.crud.order;

import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;

public class DeleteAllOrders {
    private static final String BASE_URL = "http://localhost:8080/api/order/deleteall";

    public static void runTest() {
        HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "DELETE", null);
        if (response != null) {
            System.out.println("DELETE all " + response.body());
        } else {
            System.out.println("DELETE request failed:");
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
