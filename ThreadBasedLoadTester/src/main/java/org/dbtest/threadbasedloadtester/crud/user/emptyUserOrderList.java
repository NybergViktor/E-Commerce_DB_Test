package org.dbtest.threadbasedloadtester.crud.user;

import org.dbtest.threadbasedloadtester.utils.HttpClientUtil;

import java.net.http.HttpResponse;

public class emptyUserOrderList {
    private static final String BASE_URL = "http://localhost:8080/api/user/clean/userorders/6816763f5b61af6e8ed702c6";

    public static void runTest() {
        HttpResponse<String> response = HttpClientUtil.sendRequest(BASE_URL, "PUT", null);
        if (response != null) {
            System.out.println("Emtpy all orders " + response.body());
        } else {
            System.out.println("Request failed:");
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
