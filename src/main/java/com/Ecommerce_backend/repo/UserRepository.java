package com.Ecommerce_backend.repo;

import com.Ecommerce_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String email);
}
