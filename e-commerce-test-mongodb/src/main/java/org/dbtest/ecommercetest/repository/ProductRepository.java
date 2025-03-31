package org.dbtest.ecommercetest.repository;

import org.dbtest.ecommercetest.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
