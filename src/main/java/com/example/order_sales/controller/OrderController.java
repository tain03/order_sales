package com.example.order_sales.controller;

import com.example.order_sales.dto.OrderDTO;
import com.example.order_sales.dto.OrderItemDTO;
import com.example.order_sales.entity.Customer;
import com.example.order_sales.entity.Order;
import com.example.order_sales.entity.OrderItem;
import com.example.order_sales.service.OrderService;
import com.example.order_sales.service.CustomerService;
import com.example.order_sales.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, CustomerService customerService, EmailService emailService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders in the system")
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Place a new order", description = "Create a new order and customer if necessary")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO placeOrder(@RequestBody OrderDTO orderDTO) {
        Customer customer = customerService.findOrCreateCustomer(
                orderDTO.getCustomerId(),
                orderDTO.getCustomerName(),
                orderDTO.getCustomerEmail(),
                orderDTO.getCustomerPhone(),
                orderDTO.getShippingAddress()
        );

        Order newOrder = orderService.createOrder(customer, orderDTO);

        List<OrderItemDTO> orderItemDTOs = newOrder.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        String orderDetails = generateOrderDetails(newOrder);

        emailService.sendOrderConfirmationEmail(customer.getEmail(), orderDetails);

        return new OrderDTO(
                newOrder.getOrderId(),
                newOrder.getCustomer().getCustomerId(),
                newOrder.getCustomer().getFullName(),
                newOrder.getCustomer().getEmail(),
                newOrder.getCustomer().getPhone(),
                newOrder.getOrderDate(),
                newOrder.getTotalAmount(),
                newOrder.getShippingAddress(),
                newOrder.getShippingMethod(),
                newOrder.getPaymentMethod(),
                newOrder.getNotes(),
                orderItemDTOs
        );
    }

    private String generateOrderDetails(Order order) {
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
