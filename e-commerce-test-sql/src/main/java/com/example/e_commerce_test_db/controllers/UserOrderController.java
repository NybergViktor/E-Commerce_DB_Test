package com.example.e_commerce_test_db.controllers;

import com.example.e_commerce_test_db.dto.CreateOrderRequestDTO;
import com.example.e_commerce_test_db.dto.OrderDTO;
import com.example.e_commerce_test_db.models.UserOrder;
import com.example.e_commerce_test_db.repositories.OrderRepository;
import com.example.e_commerce_test_db.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class UserOrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public UserOrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<UserOrder> createOrder(@RequestBody CreateOrderRequestDTO dto) {
        UserOrder createdUserOrder = orderService.createOrder(dto.getProductIds(), dto.getBuyer_id());
        return new ResponseEntity<>(createdUserOrder, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error finding all orders: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok(new OrderDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteall")
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

}
