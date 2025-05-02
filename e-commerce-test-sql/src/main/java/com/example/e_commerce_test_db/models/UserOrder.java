package com.example.e_commerce_test_db.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_orders")
@Data
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private AppUser buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime orderDate = LocalDateTime.now();

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public void addItem(Product product, int quantity) {
        OrderItem newItem = new OrderItem();
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setOrder(this);

        items.add(newItem);
    }
}
