package com.example.e_commerce_test_db.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDTO {
    @JsonAlias({"products", "productIds"})
    private List<Long> productIds;
    @JsonAlias({"buyer_id", "buyerId"})
    private Long buyerId;

    public Long getBuyer_id() {
        return buyerId;
    }
}

