package com.example.order_sales.service;

import com.example.order_sales.dto.OrderDTO;
import com.example.order_sales.dto.OrderItemDTO;
import com.example.order_sales.entity.Customer;
import com.example.order_sales.entity.Order;
import com.example.order_sales.entity.OrderItem;
import com.example.order_sales.entity.Product;
import com.example.order_sales.repository.OrderRepository;
import com.example.order_sales.repository.CustomerRepository;
import com.example.order_sales.repository.OrderItemRepository;
import com.example.order_sales.repository.ProductRepository;
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
    private final ProductRepository productRepository;  // Injected ProductRepository

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;  // Initialize ProductRepository
    }

    /**
     * Retrieves all orders as a list of OrderDTOs.
     * @return a list of OrderDTO objects.
     */
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getOrderId(),
                        order.getCustomer().getCustomerId(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getEmail(),
                        order.getCustomer().getPhone(),
                        order.getOrderDate(),
                        order.getTotalAmount(),
                        order.getShippingAddress(),
                        order.getShippingMethod(),
                        order.getPaymentMethod(),
                        order.getNotes(),
                        order.getOrderItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getProduct().getProductId(),
                                        item.getProduct().getProductName(),
                                        item.getPrice(),
                                        item.getQuantity()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order createOrder(Customer customer, OrderDTO orderDTO) {
        Order newOrder = new Order();
        newOrder.setCustomer(customer);

        newOrder.setOrderDate(LocalDateTime.now());

        newOrder.setShippingAddress(orderDTO.getShippingAddress());
        newOrder.setShippingMethod(orderDTO.getShippingMethod());
        newOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        newOrder.setNotes(orderDTO.getNotes());

        Double totalAmount = 0.0;

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
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

        Order savedOrder = orderRepository.save(newOrder);

        return savedOrder;
    }

}
