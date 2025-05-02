package com.example.e_commerce_test_db.models;

import com.example.e_commerce_test_db.models.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.Data;
import org.apache.catalina.User;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private UserOrder order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
