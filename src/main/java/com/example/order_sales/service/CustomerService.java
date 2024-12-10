package com.example.order_sales.service;

import com.example.order_sales.entity.Customer;
import com.example.order_sales.exception.BadRequestException;
import com.example.order_sales.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findOrCreateCustomer(Long customerId, String customerName, String customerEmail, String customerPhone, String shippingAddress) {
        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty!");
        }

        if (customerId != null) {
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }

        Customer newCustomer = Customer.builder()
                .fullName(customerName)
                .email(customerEmail)
                .phone(customerPhone)
                .address(shippingAddress)
                .build();

        return customerRepository.save(newCustomer);
    }
}
