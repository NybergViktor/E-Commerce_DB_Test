package org.dbtest.ecommercetest.services;

import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.model.User;
import org.dbtest.ecommercetest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void cleanUserOrders(String userId) {
        Optional<User> user = userRepository.findById(userId);

        User u = user.get();
        List<String> orderList = u.getUserOrders();
        orderList.clear();
        u.setUserOrders(orderList);
        userRepository.save(u);
    }
}
