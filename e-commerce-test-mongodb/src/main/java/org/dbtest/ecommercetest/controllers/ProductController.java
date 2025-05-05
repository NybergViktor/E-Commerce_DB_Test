package org.dbtest.ecommercetest.controllers;

import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.repository.ProductRepository;
import org.dbtest.ecommercetest.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    private final ProductRepository productRepository;

    //
    public ProductController(ProductService service, ProductRepository productRepository) {
        this.service = service;
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return service.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAllProducts() {
        try {
            productRepository.deleteAll();
            return ResponseEntity.ok("All products deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting products: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "100000") double maxPrice,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        try {
            return ResponseEntity.ok(service.advancedSearch(name, minPrice, maxPrice, sortBy, ascending));

        } catch (Exception e) {
            e.printStackTrace(); // <- detta behövs!
            return ResponseEntity.status(500).body("Error searching products: " + e.getMessage());
        }

    }
    

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


}
