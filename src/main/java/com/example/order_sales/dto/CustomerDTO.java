package com.example.order_sales.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
}
