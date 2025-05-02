package com.example.e_commerce_test_db.controllers;

import com.example.e_commerce_test_db.dto.CreateUserRequestDTO;
import com.example.e_commerce_test_db.models.AppUser;
import com.example.e_commerce_test_db.repositories.UserRepository;
import com.example.e_commerce_test_db.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AppUserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<AppUser> registerUser(@RequestBody CreateUserRequestDTO dto) {
        AppUser createdUser = userService.registerUser(dto.getName(), dto.getOrderIds());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/deleteall")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
