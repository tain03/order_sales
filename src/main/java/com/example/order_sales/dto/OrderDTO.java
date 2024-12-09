package com.example.order_sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private CustomerDTO customer;
    private OrderDetailsDTO order;
    private List<OrderItemDTO> orderItems;
}
