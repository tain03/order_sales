package com.example.order_sales.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
public class OrderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private Order order;

    private String previousStatus;
    private String currentStatus;
    private LocalDateTime changedAt;
    private String notes;
}
