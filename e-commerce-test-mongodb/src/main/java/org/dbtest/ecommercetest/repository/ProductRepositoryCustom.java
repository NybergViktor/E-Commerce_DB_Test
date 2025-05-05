package org.dbtest.ecommercetest.repository;

import org.dbtest.ecommercetest.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> search(String name, double minPrice, double maxPrice, String sortBy, boolean ascending);
}