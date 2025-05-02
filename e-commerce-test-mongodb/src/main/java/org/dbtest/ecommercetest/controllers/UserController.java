package org.dbtest.ecommercetest.controllers;

import org.dbtest.ecommercetest.dto.OrderDTO;
import org.dbtest.ecommercetest.model.Order;
import org.dbtest.ecommercetest.model.User;
import org.dbtest.ecommercetest.repository.OrderRepository;
import org.dbtest.ecommercetest.repository.UserRepository;
import org.dbtest.ecommercetest.services.OrderService;
import org.dbtest.ecommercetest.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return ResponseEntity.ok("All users deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting users: " + e.getMessage());
        }
    }
}
