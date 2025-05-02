package com.example.e_commerce_test_db.services;

import com.example.e_commerce_test_db.models.AppUser;
import com.example.e_commerce_test_db.models.UserOrder;
import com.example.e_commerce_test_db.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser registerUser(String name, List<Long> orderIds) {
        AppUser user = new AppUser();
        user.setName(name);
        // orderIds ignoreras – orders kopplas via OrderService, inte manuellt
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void addOrderToUser(AppUser user, UserOrder order) {
        user.getOrders().add(order);
        order.setBuyer(user);
        userRepository.save(user);
    }
}
