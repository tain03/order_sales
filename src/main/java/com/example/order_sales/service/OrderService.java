package com.example.order_sales.service;

import com.example.order_sales.dto.CustomerDTO;
import com.example.order_sales.dto.OrderDTO;
import com.example.order_sales.dto.OrderDetailsDTO;
import com.example.order_sales.dto.OrderItemDTO;
import com.example.order_sales.entity.*;
import com.example.order_sales.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderTransactionRepository orderTransactionRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        OrderItemRepository orderItemRepository, ProductRepository productRepository,
                        OrderTransactionRepository orderTransactionRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderTransactionRepository = orderTransactionRepository;
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    CustomerDTO customerDTO = new CustomerDTO(
//                            order.getCustomer().getCustomerId(),
                            order.getCustomer().getFullName(),
                            order.getCustomer().getEmail(),
                            order.getCustomer().getPhone(),
                            order.getCustomer().getAddress()
                    );

                    OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(
                            order.getOrderId(),
                            order.getOrderDate(),
                            order.getTotalAmount(),
                            order.getShippingMethod(),
                            order.getPaymentMethod(),
                            order.getNotes()
                    );

                    List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                            .map(item -> new OrderItemDTO(
                                    item.getProduct().getProductId(),
                                    item.getProduct().getProductName(),
                                    item.getPrice(),
                                    item.getQuantity(),
                                    item.getPrice() * item.getQuantity()
                            ))
                            .collect(Collectors.toList());

                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setCustomer(customerDTO);
                    orderDTO.setOrder(orderDetailsDTO);
                    orderDTO.setOrderItems(orderItemDTOs);

                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Order createOrder(Customer customer, OrderDTO orderDTO) {
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(orderDTO.getCustomer().getShippingAddress());
        newOrder.setShippingMethod(orderDTO.getOrder().getShippingMethod());
        newOrder.setPaymentMethod(orderDTO.getOrder().getPaymentMethod());
        newOrder.setNotes(orderDTO.getOrder().getNotes());

        Double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product with ID " + itemDTO.getProductId() + " not found"));

            Double productPrice = product.getPrice();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(product);
            orderItem.setPrice(productPrice);
            orderItem.setQuantity(itemDTO.getQuantity());
            newOrder.getOrderItems().add(orderItem);
            totalAmount += productPrice * itemDTO.getQuantity();
            orderItemRepository.save(orderItem);
        }

        newOrder.setTotalAmount(totalAmount);
        return orderRepository.save(newOrder);
    }

    /**
     * Updates the status of an order and creates a new transaction record for the change.
     * @param orderId the ID of the order
     * @param newStatus the new status to set
     * @param notes optional notes for the status change (e.g., reason)
     */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String notes) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderTransaction lastTransaction = orderTransactionRepository.findFirstByOrderOrderByChangedAtDesc(order);
        OrderStatus previousStatus = (lastTransaction != null) ? lastTransaction.getCurrentStatus() : OrderStatus.CREATED;

        OrderTransaction orderTransaction = new OrderTransaction();

        orderTransaction.setOrder(order);
        orderTransaction.setPreviousStatus(previousStatus);
        orderTransaction.setCurrentStatus(newStatus);
        orderTransaction.setChangedAt(LocalDateTime.now());
        orderTransaction.setNotes(notes);

        orderTransactionRepository.save(orderTransaction);
    }
}
