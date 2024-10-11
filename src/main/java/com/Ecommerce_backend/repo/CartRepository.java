package com.Ecommerce_backend.repo;

import com.Ecommerce_backend.entity.Cart;
import com.Ecommerce_backend.entity.Product;
import com.Ecommerce_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

}