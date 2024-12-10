package com.example.order_sales.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
public class OrderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus currentStatus;

    private LocalDateTime changedAt;
    private String notes;
}
