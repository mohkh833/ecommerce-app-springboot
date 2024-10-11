package com.Ecommerce_backend.service.impl;

import com.Ecommerce_backend.dto.CartDTO;
import com.Ecommerce_backend.dto.OrderDTO;
import com.Ecommerce_backend.dto.PaymentInfoRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.*;
import com.Ecommerce_backend.exception.OurException;
import com.Ecommerce_backend.repo.CartRepository;
import com.Ecommerce_backend.repo.OrderRepository;
import com.Ecommerce_backend.repo.UserRepository;
import com.Ecommerce_backend.service.interfac.IOrderService;
import com.Ecommerce_backend.utils.Utils;
import com.stripe.model.PaymentIntent;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    public Response createOrder(int userId, ShippingInfo shippingInfo, PaymentMethod paymentMethod, PaymentInfoRequest paymentInfoRequest) {

        Response response = new Response();
        try {

            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("User not found"));


            Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new OurException("Cart not found"));


            if (cart.getItems().isEmpty()) {
                throw new OurException("Cannot create order with an empty cart.");
            }

            Order order = new Order();
            order.setUser(user);
            order.setShippingInfo(shippingInfo);
            order.setPaymentMethod(paymentMethod);

            ArrayList<OrderItem> orderItems = new ArrayList<>();
            cart.getItems().forEach(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(cartItem.getProduct().getProductPrice());
                orderItem.setTotalPrice(cartItem.getProduct().getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            });

            order.setOrderItem(orderItems);
            order.setTotalPrice(cart.getTotalPrice());
            if (paymentMethod.name().equals("CREDIT_CARD")) {
                // Create Stripe payment intent
                PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);

                // Save PaymentIntent ID in the order
                order.setStripePaymentIntentId(paymentIntent.getId());

                // Set the initial status of the order to pending
                order.setOrderStatus(OrderStatus.PENDING);
            } else if (paymentMethod.name().equals("CASH_ON_DELIVERY")) {
                // Set status to confirmed for cash on delivery
                order.setOrderStatus(OrderStatus.CONFIRMED);
            }


            // Save the order
            orderRepository.save(order);

            cartRepository.delete(cart);

            OrderDTO orderDTO = Utils.mapOrderEntityToOrderDTO(order);
            response.setOrder(orderDTO);
            response.setStatusCode(200);
            response.setMessage("Order created successfully");

        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Adding product: " + e.getMessage());
        }
        return response;
    }

    public Response deleteOrder(int orderId) {
        Response response = new Response();
        try {
            Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new OurException("Order not found"));

            orderRepository.delete(order);

            response.setStatusCode(200);
            response.setMessage("Order deleted successfully");

        } catch (OurException e) {
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during Deleting Order: " + e.getMessage());
        }


        return response;
    }

    public Response viewOrdersHistory(int userId) {
        Response response = new Response();
        try {

            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("User not found"));

            List<Order> orders = orderRepository.findByUser(user);

            response.setOrders(Utils.mapOrderListEntityToOrderListDTO(orders));

            response.setStatusCode(200);
            response.setMessage("Order history retrieved successfully");
        } catch (OurException e) {
            // Log the exception and handle it gracefully
            e.printStackTrace();  // Or use a logger
            handleException(response, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            // Log the exception to help identify the issue
            e.printStackTrace();  // Or use a logger
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while retrieving order history: " + e.getMessage());
        }

        return response;
    }


    private void handleException(Response response, HttpStatus status, String message) {
        response.setStatusCode(status.value());
        response.setMessage(message);
    }
}
