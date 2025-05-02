package com.example.e_commerce_test_db.repositories;

import com.example.e_commerce_test_db.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
