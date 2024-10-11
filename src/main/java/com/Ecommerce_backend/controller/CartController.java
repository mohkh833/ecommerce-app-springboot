package com.Ecommerce_backend.controller;

import com.Ecommerce_backend.dto.CartDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.service.interfac.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<Response> addCart(@RequestBody CartDTO cartDTO) {
        Response response = cartService.addItemToCart(cartDTO.getQuantity(),cartDTO.getProduct(),cartDTO.getUserId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-my-carts")
    public ResponseEntity<Response> getMyCarts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Response response = cartService.getMyCarts(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping ("/update-cart-quantity")
    public ResponseEntity<Response> updateItemQuantity(@RequestBody CartDTO cartDTO) {
        Response response = cartService.updateItemQuantity(cartDTO.getQuantity(),cartDTO.getProduct(),cartDTO.getUserId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/remove-item-from-cart")
    public ResponseEntity<Response> removeItemFromCart(@RequestBody CartDTO cartDTO) {
        Response response = cartService.removeItemFromCart(cartDTO.getUserId(),cartDTO.getProduct());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/clear-cart")
    public ResponseEntity<Response> clearCart(@RequestBody CartDTO cartDTO) {
        Response response = cartService.clearCart(cartDTO.getUserId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
