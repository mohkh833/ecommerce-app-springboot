package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.Response;

public interface ICategoryService {
    Response addCategory(String name);
    Response editCategory(Long categoryId, String newName);
    Response deleteCategory(Long categoryId);
    Response getAllCategories();
}
