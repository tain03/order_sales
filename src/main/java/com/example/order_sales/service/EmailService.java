package com.example.order_sales.service;

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
        mailMessage.setText(orderDetails); // You can use HTML formatting as needed

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Log error or notify the admin
        }
    }
}
