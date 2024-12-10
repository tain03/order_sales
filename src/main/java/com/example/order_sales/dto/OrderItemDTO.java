package com.example.order_sales.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalAmount;
}
