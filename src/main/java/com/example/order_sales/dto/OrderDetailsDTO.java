package com.example.order_sales.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDetailsDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingMethod;
    private String paymentMethod;
    private String notes;
}
