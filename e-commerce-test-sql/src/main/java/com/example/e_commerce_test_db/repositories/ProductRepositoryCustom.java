package com.example.e_commerce_test_db.repositories;

import com.example.e_commerce_test_db.models.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> advancedSearch(String name, double minPrice, double maxPrice, String sortBy, boolean ascending);
}
