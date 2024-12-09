package com.example.order_sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingMethod;
    private String paymentMethod;
    private String notes;
}
