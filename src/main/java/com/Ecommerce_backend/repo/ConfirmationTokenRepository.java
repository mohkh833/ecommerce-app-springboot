package com.Ecommerce_backend.repo;

import com.Ecommerce_backend.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}