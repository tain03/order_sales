package com.example.order_sales.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String fullName;
    private String email;
    private String phone;
    private String address;


}
