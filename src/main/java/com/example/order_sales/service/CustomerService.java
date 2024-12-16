package com.example.order_sales.service;

import com.example.order_sales.entity.Customer;
import com.example.order_sales.exception.BadRequestException;
import com.example.order_sales.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer findOrCreateCustomer(Long customerId, String customerName, String customerEmail, String customerPhone, String shippingAddress) {
        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty!");
        }
        if (customerName == null || customerName.isEmpty()) {
            throw new BadRequestException("Name cannot be null or empty!");
        }
        if (customerPhone == null || customerPhone.isEmpty()) {
            throw new BadRequestException("Phone number cannot be null or empty!");
        }
        if (shippingAddress == null || shippingAddress.isEmpty()) {
            throw new BadRequestException("Shipping address cannot be null or empty!");
        }

        if (customerId != null) {
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }  else {
            Optional<Customer> existingCustomer = customerRepository.findByEmail(customerEmail);
            if (existingCustomer.isPresent()) {
                return existingCustomer.get();
            } else {
                Customer newCustomer = Customer.builder()
                        .fullName(customerName)
                        .email(customerEmail)
                        .phone(customerPhone)
                        .address(shippingAddress)
                        .build();
                return customerRepository.save(newCustomer);
            }
        }
    }
}