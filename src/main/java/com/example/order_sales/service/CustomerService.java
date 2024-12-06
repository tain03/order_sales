package com.example.order_sales.service;

import com.example.order_sales.entity.Customer;
import com.example.order_sales.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findOrCreateCustomer(Long customerId, String fullName, String email, String phone, String address) {
        Customer customer;
        if (customerId != null) {
            customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        } else {
            customer = new Customer();
            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);

            customer = customerRepository.save(customer);
        }

        return customer;
    }
}
