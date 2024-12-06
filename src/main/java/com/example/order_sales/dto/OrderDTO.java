package com.example.order_sales.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String notes;
    private List<OrderItemDTO> items;

    public OrderDTO(Long orderId, Long customerId, String customerName, String customerEmail, String customerPhone,
                    LocalDateTime orderDate, Double totalAmount, String shippingAddress, String shippingMethod,
                    String paymentMethod, String notes, List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.items = items;
    }
}
