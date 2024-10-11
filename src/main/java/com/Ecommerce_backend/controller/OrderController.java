package com.Ecommerce_backend.controller;

import com.Ecommerce_backend.dto.OrderDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.PaymentMethod;
import com.Ecommerce_backend.entity.ShippingInfo;
import com.Ecommerce_backend.entity.User;
import com.Ecommerce_backend.service.interfac.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<Response> createOrder(@RequestBody OrderDTO orderDTO) {
        Response response = orderService.createOrder(orderDTO.getUserId(),orderDTO.getShippingInfo(),orderDTO.getPaymentMethod(), orderDTO.getPaymentInfoRequest());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-order/{orderId}")
    public ResponseEntity<Response> deleteOrder(@PathVariable("orderId") int orderId){
        Response response = orderService.deleteOrder(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-order-history/{userId}")
    public ResponseEntity<Response> getAllOrders(@PathVariable("userId") int userId) {
        Response response = orderService.viewOrdersHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
