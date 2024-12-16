package com.example.order_sales.service;

import com.example.order_sales.entity.Order;
import com.example.order_sales.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOrderConfirmationEmail(String customerEmail, String orderDetails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(customerEmail);
        mailMessage.setSubject("Order Confirmation - Your Order #");
        mailMessage.setText(orderDetails);

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateOrderDetails(Order order) {
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Customer: ").append(order.getCustomer().getFullName()).append("\n");
        orderDetails.append("Phone Number: ").append(order.getCustomer().getPhone()).append("\n");
        orderDetails.append("Shipping Address: ").append(order.getShippingAddress()).append("\n");
        orderDetails.append("Shipping Method: ").append(order.getShippingMethod()).append("\n");
        orderDetails.append("Payment Method: ").append(order.getPaymentMethod()).append("\n");
        orderDetails.append("Order Date: ").append(order.getOrderDate()).append("\n");
        orderDetails.append("Total Amount: ").append(order.getTotalAmount()).append("\n");
        orderDetails.append("\nOrder Items:\n");

        for (OrderItem item : order.getOrderItems()) {
            orderDetails.append(item.getProduct().getProductName())
                    .append(" - Quantity: ").append(item.getQuantity())
                    .append(" - Price: ").append(item.getPrice())
                    .append("\n");
        }

        return orderDetails.toString();
    }
}
