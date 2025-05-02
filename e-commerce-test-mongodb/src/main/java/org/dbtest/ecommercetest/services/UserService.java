package org.dbtest.ecommercetest.services;

import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.model.User;
import org.dbtest.ecommercetest.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }
}
