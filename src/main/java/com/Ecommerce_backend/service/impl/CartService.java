package com.Ecommerce_backend.service.impl;

import com.Ecommerce_backend.dto.CartDTO;
import com.Ecommerce_backend.dto.ProductDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.dto.UserDTO;
import com.Ecommerce_backend.entity.Cart;
import com.Ecommerce_backend.entity.CartItem;
import com.Ecommerce_backend.entity.Product;
import com.Ecommerce_backend.entity.User;
import com.Ecommerce_backend.exception.OurException;
import com.Ecommerce_backend.repo.CartRepository;
import com.Ecommerce_backend.repo.UserRepository;
import com.Ecommerce_backend.service.interfac.ICartService;
import com.Ecommerce_backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

import static org.aspectj.runtime.internal.Conversions.intValue;

@Service
public class CartService implements ICartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    public Response getMyCarts(String username) {
        Response response = new Response();

        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new OurException("Username not found"));
            Cart cart = getCartByUser(user.getId());
            cart.recalculateTotalPrice();
            CartDTO cartDto = Utils.mapCartItemsToCartDTO(cart);

            response.setCart(cartDto);
            response.setStatusCode(200);
            response.setMessage("Cart is returned successfully");

        }  catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Adding product: " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response addItemToCart(int quantity, Product product, int userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("User not found"));

            Cart cart = cartRepository.findByUser(user)
                    .orElseGet(() -> createNewCart(user));

            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if(existingItem.isPresent()){
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setUnitPrice(product.getProductPrice());
                newItem.setQuantity(quantity);
                newItem.setCart(cart);
                cart.addItem(newItem);
                cart.recalculateTotalPrice();
            }
            cartRepository.save(cart);
            response.setStatusCode(200);
            response.setMessage("Product added to cart successfully");
        }

        catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Adding product: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateItemQuantity(int quantity, Product product, int userId) {
        Response response = new Response();
        try{
            Cart cart = getCartByUser(Long.valueOf(userId));
            CartItem item = findCartItemByProduct(cart,product.getId());

            if(quantity <= 0) {
                cart.removeItem(item);
            } else {
                item.setQuantity(quantity);
            }
            cart.recalculateTotalPrice();
            cartRepository.save(cart);
            response.setStatusCode(200);
            response.setMessage("quantity updated successfully");

        }  catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during updating item quantity: " + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response removeItemFromCart(int userId, Product product) {
        Response response = new Response();
        try {
            Cart cart = getCartByUser(Long.valueOf(userId));
            CartItem item = findCartItemByProduct(cart, product.getId());
            cart.removeItem(item);
            cart.recalculateTotalPrice();
            cartRepository.delete(cart);
            response.setStatusCode(200);
            response.setMessage("cart removed successfully");
        }  catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during removing item from cart: " + e.getMessage());
        }
        return  response;
    }

    @Override
    public Response clearCart(int userId) {
        Response response = new Response();
        try {
            Cart cart = getCartByUser(Long.valueOf(userId));
            cart.getItems().clear();
            cartRepository.delete(cart);
            response.setStatusCode(200);
            response.setMessage("cart removed successfully");
        }  catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Adding product: " + e.getMessage());
        }
        return  response;
    }

    private CartItem findCartItemByProduct(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new OurException("Product not found in cart"));
    }


    private Cart getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OurException("User not found"));
        return cartRepository.findByUser(user).orElseGet(() -> createNewCart(user));
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }



    private void handleException(Response response, HttpStatus status, String message) {
        response.setStatusCode(status.value());
        response.setMessage(message);
    }

}
