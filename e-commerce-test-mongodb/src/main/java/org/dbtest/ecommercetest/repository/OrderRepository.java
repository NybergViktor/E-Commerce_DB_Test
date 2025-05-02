package org.dbtest.ecommercetest.repository;

import org.dbtest.ecommercetest.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
