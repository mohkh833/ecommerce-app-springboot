package com.Ecommerce_backend.utils;

import com.Ecommerce_backend.dto.*;
import com.Ecommerce_backend.entity.*;
//import com.Ecommerce_backend.entity.Review;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.stream.Collectors;

import static org.aspectj.runtime.internal.Conversions.intValue;

public class Utils {

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

//    public static ReviewDTO mapReviewEntitytoReviewDTO(Review review) {
//        ReviewDTO reviewDTO = new ReviewDTO();
//        reviewDTO.setReviewDescription(review.getReviewDescription());
//        reviewDTO.setRate(review.getRating());
//        return reviewDTO;
//    }

    public static ProductDTO mapProductEntityToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setDesc(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setProductPrice(product.getProductPrice());
        List<String> categoryNames = product.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        productDTO.setCategories(categoryNames);
        productDTO.setInStock(product.isInStock());
        return productDTO;
    }

    public static OrderDTO mapOrderEntityToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setShippingInfo(order.getShippingInfo());
        orderDTO.setUserId(order.getUser().getId().intValue());
        List<OrderItemDTO> orderItemsDTO = order.getOrderItem().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getId(),            // Product ID instead of full Product
                        item.getProduct().getTitle(),         // Product title (or any other relevant field)
                        item.getId(),                         // Order Item ID
                        item.getOrder().getId(),              // Order ID instead of full Order object
                        item.getQuantity(),
                        item.getTotalPrice(),
                        item.getUnitPrice()))
                .collect(Collectors.toList());

        orderDTO.setOrderItems(orderItemsDTO);


        return orderDTO;
    }

    public static CartDTO mapCartItemsToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(intValue(cart.getUser().getId()));  // Keep it as Long
        cartDTO.setShippingCost(intValue(cart.getShippingCost()));
        // Assuming CartItemDTO is a DTO representation of CartItem
        List<CartItemDTO> cartItemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(item.getId(), item.getProduct().getId(),item.getProduct().getTitle(),item.getUnitPrice(),item.getQuantity(),item.getTotalPrice()))
                .collect(Collectors.toList());

        cartDTO.setCartItems(cartItemDTOs);
        cartDTO.setTotalPrice(intValue(cart.getTotalPrice()));
        cartDTO.setCartStatus(cart.getStatus().toString());

        return cartDTO;
    }


    public static CategoryDTO mapCategoryItemToCategoryDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setId(category.getId());
        return categoryDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserProducts(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (!user.getProducts().isEmpty()) {
            userDTO.setProducts(user.getProducts().stream().map(product -> mapProductEntityToProductDTO(product)).collect(Collectors.toList()));
        }
        return userDTO;
    }


    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<ProductDTO> mapProductListEntityToProductListDTO(List<Product> productList) {
        return productList.stream().map(Utils::mapProductEntityToProductDTO).collect(Collectors.toList());
    }

    public static List<OrderDTO> mapOrderListEntityToOrderListDTO(List<Order> orderList) {
        return orderList.stream().map(Utils::mapOrderEntityToOrderDTO).collect(Collectors.toList());
    }

    public static List<CategoryDTO> mapCategoryListEntityToCategoryListDTO(List<Category> categoryList) {
        return categoryList.stream().map(Utils:: mapCategoryItemToCategoryDTO).collect(Collectors.toList());
    }



//    public static List<ReviewDTO> mapReviewListEntityToProductListDTO(List<Review> reviewList) {
//        return reviewList.stream().map(Utils::mapReviewEntitytoReviewDTO).collect(Collectors.toList());
//    }
}
