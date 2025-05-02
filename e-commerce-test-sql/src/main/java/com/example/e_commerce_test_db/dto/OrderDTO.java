package com.example.e_commerce_test_db.dto;

import com.example.e_commerce_test_db.models.UserOrder;
import com.example.e_commerce_test_db.models.AppUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {

    private Long id;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
    private double totalPrice;
    private AppUser buyer;

    public OrderDTO(UserOrder userOrder) {
        this.id = userOrder.getId();
        this.orderDate = userOrder.getOrderDate();
        this.items = userOrder.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )).collect(Collectors.toList());
        this.totalPrice = userOrder.getTotalPrice();
        this.buyer = userOrder.getBuyer();
    }

}
