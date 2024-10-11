package com.Ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;

    private BigDecimal shippingCost = BigDecimal.valueOf(25);


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status = CartStatus.ACTIVE;

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        recalculateTotalPrice();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        totalPrice = items.stream()
                .map((CartItem::getTotalPrice))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalPrice = totalPrice.add(shippingCost);
    }
    public enum CartStatus {
        ACTIVE,
        ORDERED,
        CANCELED
    }

}
