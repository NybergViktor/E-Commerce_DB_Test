package org.dbtest.threadbasedloadtester.crud;

import org.dbtest.threadbasedloadtester.utils.*;

import java.net.http.HttpResponse;

public class Get {
    public static CrudUtils crudUtils = new CrudUtils();
    private static final String BASE_URL = "http://localhost:8080/api/products";

    public static void runTest() {
        HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "GET", null);
        if (response != null) {
            System.out.println("GET response: " + response.body());
        } else {
            System.out.println("GET request failed.");
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
