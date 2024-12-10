package com.example.order_sales.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    private CustomerDTO customer;
    private OrderDetailsDTO order;
    private List<OrderItemDTO> orderItems;
}
