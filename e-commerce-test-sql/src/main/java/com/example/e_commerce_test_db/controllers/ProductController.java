package com.example.e_commerce_test_db.controllers;

import com.example.e_commerce_test_db.models.Product;
import com.example.e_commerce_test_db.repositories.ProductRepository;
import com.example.e_commerce_test_db.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Hämta alla produkter, sorterade efter pris (högst till lägst)
    @GetMapping
    public List<Product> getAllProductsSortedByPrice() {
        return productService.getAllProductsSortedByPriceDesc();
    }

    // Sök produkter efter namn
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
    @DeleteMapping
    public void deleteAll() {
        productRepository.deleteAll();
    }

    // Lägg till en ny produkt
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
}