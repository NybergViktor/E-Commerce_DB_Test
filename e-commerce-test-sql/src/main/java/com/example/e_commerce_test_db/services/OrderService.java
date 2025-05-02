package com.example.e_commerce_test_db.services;

import com.example.e_commerce_test_db.models.AppUser;
import com.example.e_commerce_test_db.models.Product;
import com.example.e_commerce_test_db.models.UserOrder;
import com.example.e_commerce_test_db.repositories.OrderRepository;
import com.example.e_commerce_test_db.repositories.ProductRepository;
import com.example.e_commerce_test_db.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public UserOrder createOrder(List<Long> productIds, Long userId) {
        UserOrder userOrder = new UserOrder();

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            userOrder.addItem(product, 1); // Quantity default = 1, eller ändra om du vill stödja flera
        }

        AppUser buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        userOrder.setBuyer(buyer);

        return orderRepository.save(userOrder);
    }

    public List<UserOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<UserOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
