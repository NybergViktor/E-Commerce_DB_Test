package org.dbtest.ecommercetest.services;

import org.dbtest.ecommercetest.dto.OrderDTO;
import org.dbtest.ecommercetest.model.Order;
import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.model.User;
import org.dbtest.ecommercetest.repository.OrderRepository;
import org.dbtest.ecommercetest.repository.ProductRepository;
import org.dbtest.ecommercetest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    public Order addOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setProducts(orderDTO.getProducts());
        User user = userRepository.findById(orderDTO.getBuyer_id()).get();
        List<String> userOders = user.getUserOrders();
        userOders.add(orderDTO.getProducts().toString());
        order.setBuyer_id(orderDTO.getBuyer_id());
        userRepository.save(user);
        return orderRepository.save(order);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
