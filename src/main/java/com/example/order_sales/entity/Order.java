package com.example.order_sales.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}
