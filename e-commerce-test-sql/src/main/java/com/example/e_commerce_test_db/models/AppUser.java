package com.example.e_commerce_test_db.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_users")
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrder> userOrders = new ArrayList<>();

    public Collection<UserOrder> getOrders() {
        return userOrders;
    }
}
