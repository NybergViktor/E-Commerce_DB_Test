package org.dbtest.ecommercetest.services;

import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product getProductById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public Product updateProduct(String id, Product newProduct) {
        return repository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(newProduct.getName());
                    existingProduct.setDescription(newProduct.getDescription());
                    existingProduct.setPrice(newProduct.getPrice());
                    existingProduct.setStock(newProduct.getStock());
                    return repository.save(existingProduct);
                })
                .orElse(null);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<Product> advancedSearch(String name, double min, double max, String sortBy, boolean ascending) {
        return repository.search(name, min, max, sortBy, ascending);
    }
}
