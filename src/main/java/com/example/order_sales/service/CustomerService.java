package com.example.order_sales.service;

import com.example.order_sales.entity.Customer;
import com.example.order_sales.exception.BadRequestException;
import com.example.order_sales.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer findOrCreateCustomer(Long customerId, String customerName, String customerEmail, String customerPhone, String shippingAddress) {
        // Validate input attributes to ensure they are not null or empty
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

        // If customerId is provided, attempt to fetch existing customer
        if (customerId != null) {
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }

        // Create new Customer if not found
        Customer newCustomer = Customer.builder()
                .fullName(customerName)
                .email(customerEmail)
                .phone(customerPhone)
                .address(shippingAddress)
                .build();

        // Save the new customer and return
        return customerRepository.save(newCustomer);
    }
}
