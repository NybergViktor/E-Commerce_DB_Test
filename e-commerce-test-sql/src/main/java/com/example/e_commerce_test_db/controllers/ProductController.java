package com.example.e_commerce_test_db.controllers;

import com.example.e_commerce_test_db.models.Product;
import com.example.e_commerce_test_db.repositories.ProductRepository;
import com.example.e_commerce_test_db.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        try {
            return ResponseEntity.ok(productRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error finding all products: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "100000") double maxPrice,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        try {
            List<Product> results = productService.advancedSearch(name, minPrice, maxPrice, sortBy, ascending);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error searching products: " + e.getMessage());
        }
    }

}