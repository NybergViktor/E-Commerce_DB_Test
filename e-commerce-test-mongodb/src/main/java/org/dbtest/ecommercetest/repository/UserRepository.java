package org.dbtest.ecommercetest.repository;

import org.dbtest.ecommercetest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
