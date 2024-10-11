package com.Ecommerce_backend.repo;

import com.Ecommerce_backend.entity.Cart;
import com.Ecommerce_backend.entity.Category;
import com.Ecommerce_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);

}
