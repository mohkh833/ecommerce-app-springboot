package com.Ecommerce_backend.controller;

import com.Ecommerce_backend.dto.GettingProductRequest;
import com.Ecommerce_backend.dto.LoginRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.service.interfac.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @PostMapping("/all")
    public ResponseEntity<Response> getAllProducts(@RequestBody GettingProductRequest gettingProductRequest) {
        Response response = productService.getAllProducts(gettingProductRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add-product")
    public ResponseEntity<Response> addNewProduct(
            @RequestParam(value = "userId", required=false) String userId,
            @RequestParam(value = "photo", required=false) MultipartFile photo,
            @RequestParam(value = "title", required=false) String title,
            @RequestParam(value = "productPrice", required=false) BigDecimal productPrice,
            @RequestParam(value = "description", required=false) String description,
            @RequestParam(value = "categories", required=false) List<String> categories
    ) {
        if(photo == null || photo.isEmpty() || userId == null || title==null || productPrice == null|| description ==null || categories == null){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide all fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = productService.addProduct(userId,photo,title,productPrice, description, categories);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search-products")
    public ResponseEntity<Response> searchProducts( @RequestParam(value = "title", required=false) String title , @RequestParam(value = "description", required=false) String description) {
        Response response = productService.searchProducts(title, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-id/{productId}")
    public ResponseEntity<Response> getUserById(@PathVariable("productId") String productId) {
        Response response = productService.getProductById(productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-my-products-info")
    public ResponseEntity<Response> getLoggedInProductInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Response response = productService.getMyProducts(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("productId") String productId) {
        Response response = productService.deleteProduct(productId);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/edit-product/{productId}")
    public ResponseEntity<Response> editProduct(
            @PathVariable(value = "productId", required=false) String productId,
            @RequestParam(value = "photo", required=false) MultipartFile photo,
            @RequestParam(value = "title", required=false) String title,
            @RequestParam(value = "productPrice", required=false) BigDecimal productPrice,
            @RequestParam(value = "description", required=false) String description,
            @RequestParam(value = "categories", required=false) List<String> categories
    ) {
        Response response = productService.editProduct(productId,photo,title,productPrice, description, categories);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
