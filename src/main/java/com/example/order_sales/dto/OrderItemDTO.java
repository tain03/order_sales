package com.example.order_sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
