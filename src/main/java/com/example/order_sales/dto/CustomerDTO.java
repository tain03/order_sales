package com.example.order_sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
}
