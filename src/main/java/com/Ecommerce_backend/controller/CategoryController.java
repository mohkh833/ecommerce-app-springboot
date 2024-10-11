package com.Ecommerce_backend.controller;

import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.Category;
import com.Ecommerce_backend.exception.OurException;

import com.Ecommerce_backend.service.impl.CategoryService;
import com.Ecommerce_backend.service.interfac.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Response> addCategory(@RequestParam String name) {
        Response response = categoryService.addCategory(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Response> editCategory(@PathVariable Long id, @RequestParam String newName) {
        Response response = categoryService.editCategory(id,newName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        Response response = categoryService.deleteCategory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<Response> getAllCategories() {
        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
