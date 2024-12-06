package com.example.order_sales.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String category;
    private String size;
    private String color;
    private String imageUrl;
}
