package com.Ecommerce_backend.repo;

import com.Ecommerce_backend.entity.Cart;
import com.Ecommerce_backend.entity.Order;
import com.Ecommerce_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);


}