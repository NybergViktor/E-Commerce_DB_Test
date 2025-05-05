package org.dbtest.ecommercetest.controllers;

import org.dbtest.ecommercetest.dto.OrderDTO;
import org.dbtest.ecommercetest.model.Order;
import org.dbtest.ecommercetest.model.Product;
import org.dbtest.ecommercetest.repository.OrderRepository;
import org.dbtest.ecommercetest.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.addOrder(orderDTO);
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deleteAllOrders() {
        try {
            orderRepository.deleteAll();
            return ResponseEntity.ok("All orders deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting orders: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error finding all orders: " + e.getMessage());
        }
    }
}
