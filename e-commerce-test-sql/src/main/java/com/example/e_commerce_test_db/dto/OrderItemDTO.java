package com.example.e_commerce_test_db.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    private String productName;
    private double unitPrice;
    private double totalPrice;

    public OrderItemDTO(String productName, double unitPrice, int quantity) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }

}
