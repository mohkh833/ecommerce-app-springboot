package com.Ecommerce_backend.service.impl;

import com.Ecommerce_backend.dto.CategoryDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.Category;
import com.Ecommerce_backend.exception.OurException;
import com.Ecommerce_backend.repo.CategoryRepository;
import com.Ecommerce_backend.service.interfac.ICategoryService;
import com.Ecommerce_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response addCategory(String name) {
        Response response = new Response();
        try {
            Optional<Category> existingCategory = categoryRepository.findByName(name);
            if(existingCategory.isPresent()) {
                throw new OurException("Category already exists");
            }

            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
            CategoryDTO categoryDTO = Utils.mapCategoryItemToCategoryDTO(category);
            response.setStatusCode(200);
            response.setMessage("Successful Category Added");
            response.setCategory(categoryDTO);
        }   catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting products: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response editCategory(Long categoryId, String newName) {

        Response response = new Response();
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new OurException("Category not found"));

            if(categoryRepository.findByName(newName).isPresent()) {
                throw new OurException("Category name already exists");
            }

            category.setName(newName);
            categoryRepository.save(category);
            CategoryDTO categoryDTO = Utils.mapCategoryItemToCategoryDTO(category);
            response.setStatusCode(200);
            response.setMessage("Successful Category Updated");
            response.setCategory(categoryDTO);
        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting products: " + e.getMessage());
        }
        return response;

    }

    @Override
    public Response deleteCategory(Long categoryId) {
        Response response = new Response();
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new OurException("Category not found"));

            categoryRepository.delete(category);

            response.setStatusCode(200);
            response.setMessage("Successful Category Deleted");

        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting products: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllCategories() {
        Response response = new Response();
        try {
            List<Category> categoryList = categoryRepository.findAll();
            response.setStatusCode(200);
            response.setMessage("Successful all categories returned");
            List<CategoryDTO> categoriesDTO = Utils.mapCategoryListEntityToCategoryListDTO(categoryList);
            response.setCategories(categoriesDTO);
        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e){
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during getting products: " + e.getMessage());
        }

        return response;
    }

    private void handleException(Response response, HttpStatus status, String message) {
        response.setStatusCode(status.value());
        response.setMessage(message);
    }

}
