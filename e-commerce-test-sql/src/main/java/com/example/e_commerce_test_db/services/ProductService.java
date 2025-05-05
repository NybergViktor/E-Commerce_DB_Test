package com.example.e_commerce_test_db.services;

import com.example.e_commerce_test_db.models.Product;
import com.example.e_commerce_test_db.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> advancedSearch(String name, double minPrice, double maxPrice, String sortBy, boolean ascending) {
        return productRepository.advancedSearch(name, minPrice, maxPrice, sortBy, ascending);
    }

}
