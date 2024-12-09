package com.example.order_sales.service;

import com.example.order_sales.entity.Customer;
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
        if (customerId != null) {
            // Nếu customerId có, tìm khách hàng theo customerId
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        } else {
            // Tìm khách hàng theo email
            Optional<Customer> existingCustomer = customerRepository.findByEmail(customerEmail);

            if (existingCustomer.isPresent()) {
                // Nếu tìm thấy khách hàng, trả về khách hàng đó
                return existingCustomer.get();
            } else {
                // Nếu không tìm thấy khách hàng, tạo mới khách hàng
                Customer newCustomer = new Customer();
                newCustomer.setFullName(customerName);
                newCustomer.setEmail(customerEmail);
                newCustomer.setPhone(customerPhone);
                newCustomer.setAddress(shippingAddress);
                return customerRepository.save(newCustomer);  // Lưu và trả về khách hàng mới
            }
        }
    }
}
