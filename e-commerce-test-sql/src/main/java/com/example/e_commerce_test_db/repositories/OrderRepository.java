package com.example.e_commerce_test_db.repositories;

import com.example.e_commerce_test_db.models.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
}
