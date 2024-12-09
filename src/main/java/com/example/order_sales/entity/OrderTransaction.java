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

    @Enumerated(EnumType.STRING) // Lưu trạng thái dưới dạng chuỗi
    private OrderStatus previousStatus;  // Trạng thái cũ

    @Enumerated(EnumType.STRING) // Lưu trạng thái dưới dạng chuỗi
    private OrderStatus currentStatus;   // Trạng thái mới

    private LocalDateTime changedAt;
    private String notes;
}
