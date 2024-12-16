package com.example.order_sales.service;

import com.example.order_sales.dto.CustomerDTO;
import com.example.order_sales.dto.OrderDTO;
import com.example.order_sales.dto.OrderDetailsDTO;
import com.example.order_sales.dto.OrderItemDTO;
import com.example.order_sales.entity.*;
import com.example.order_sales.exception.BadRequestException;
import com.example.order_sales.exception.ProductNotFoundException;
import com.example.order_sales.repository.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Builder
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderTransactionRepository orderTransactionRepository;


    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    CustomerDTO customerDTO = CustomerDTO.builder()
                            .customerName(order.getCustomer().getFullName())
                            .customerEmail(order.getCustomer().getEmail())
                            .customerPhone(order.getCustomer().getPhone())
                            .shippingAddress(order.getCustomer().getAddress())
                            .build();

                    OrderDetailsDTO orderDetailsDTO = OrderDetailsDTO.builder()
                            .orderId(order.getOrderId())
                            .orderDate(order.getOrderDate())
                            .totalAmount(order.getTotalAmount())
                            .shippingMethod(order.getShippingMethod())
                            .paymentMethod(order.getPaymentMethod())
                            .notes(order.getNotes())
                            .build();

                    List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                            .map(item -> OrderItemDTO.builder()
                                    .productId(item.getProduct().getProductId())
                                    .productName(item.getProduct().getProductName())
                                    .price(item.getPrice())
                                    .quantity(item.getQuantity())
                                    .totalAmount(item.getPrice() * item.getQuantity())
                                    .build())
                            .collect(Collectors.toList());

                    return OrderDTO.builder()
                            .customer(customerDTO)
                            .order(orderDetailsDTO)
                            .orderItems(orderItemDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = {Exception.class, BadRequestException.class, ProductNotFoundException.class})
    public Order createOrder(Customer customer, OrderDTO orderDTO) {

        Customer savedCustomer = customerRepository.save(customer);

        Order newOrder = Order.builder()
                .customer(savedCustomer)
                .orderDate(LocalDateTime.now())
                .shippingAddress(orderDTO.getCustomer().getShippingAddress())
                .shippingMethod(orderDTO.getOrder().getShippingMethod())
                .paymentMethod(orderDTO.getOrder().getPaymentMethod())
                .notes(orderDTO.getOrder().getNotes())
                .orderItems(new ArrayList<>())
                .build();

        double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + itemDTO.getProductId() + " not found"));

            Double productPrice = product.getPrice();

            OrderItem orderItem = OrderItem.builder()
                    .order(newOrder)
                    .product(product)
                    .price(productPrice)
                    .quantity(itemDTO.getQuantity())
                    .build();

            newOrder.getOrderItems().add(orderItem);
            totalAmount += productPrice * itemDTO.getQuantity();
        }

        newOrder.setTotalAmount(totalAmount);

        return orderRepository.save(newOrder);
    }



    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String notes) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderTransaction lastTransaction = orderTransactionRepository.findFirstByOrderOrderByChangedAtDesc(order);
        OrderStatus previousStatus = (lastTransaction != null) ? lastTransaction.getCurrentStatus() : OrderStatus.CREATED;

        OrderTransaction orderTransaction = OrderTransaction.builder()
                .order(order)
                .previousStatus(previousStatus)
                .currentStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .notes(notes)
                .build();

        orderTransactionRepository.save(orderTransaction);
    }
}