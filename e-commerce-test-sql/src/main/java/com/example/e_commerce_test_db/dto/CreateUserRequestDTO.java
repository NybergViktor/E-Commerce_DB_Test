package com.example.e_commerce_test_db.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequestDTO {
    private String name;
    @JsonAlias({"userOrders", "order_ids"})
    private List<Long> orderIds;
}
