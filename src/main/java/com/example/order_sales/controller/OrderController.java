package com.example.order_sales.controller;

import com.example.order_sales.dto.CustomerDTO;
import com.example.order_sales.dto.OrderDTO;
import com.example.order_sales.dto.OrderDetailsDTO;
import com.example.order_sales.dto.OrderItemDTO;
import com.example.order_sales.entity.Customer;
import com.example.order_sales.entity.Order;
import com.example.order_sales.entity.OrderItem;
import com.example.order_sales.entity.OrderStatus;
import com.example.order_sales.service.OrderService;
import com.example.order_sales.service.CustomerService;
import com.example.order_sales.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
                null,
                orderDTO.getCustomer().getCustomerName(),
                orderDTO.getCustomer().getCustomerEmail(),
                orderDTO.getCustomer().getCustomerPhone(),
                orderDTO.getCustomer().getShippingAddress()
        );

        Order newOrder = orderService.createOrder(customer, orderDTO);

        List<OrderItemDTO> orderItemDTOs = newOrder.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .totalAmount(item.getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        OrderDetailsDTO orderDetails = OrderDetailsDTO.builder()
                .orderId(newOrder.getOrderId())
                .orderDate(newOrder.getOrderDate())
                .totalAmount(newOrder.getTotalAmount())
                .shippingMethod(newOrder.getShippingMethod())
                .paymentMethod(newOrder.getPaymentMethod())
                .notes(newOrder.getNotes())
                .build();

        OrderDTO response = OrderDTO.builder()
                .customer(CustomerDTO.builder()
                        .customerName(customer.getFullName())
                        .customerEmail(customer.getEmail())
                        .customerPhone(customer.getPhone())
                        .shippingAddress(customer.getAddress())
                        .build())
                .order(orderDetails)
                .orderItems(orderItemDTOs)
                .build();

        String orderDetailsText = emailService.generateOrderDetails(newOrder);
        emailService.sendOrderConfirmationEmail(customer.getEmail(), orderDetailsText);

        return response;
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order and log the transaction")
    @PutMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long orderId,
                                  @RequestParam OrderStatus newStatus,
                                  @RequestParam(required = false) String notes) {
        orderService.updateOrderStatus(orderId, newStatus, notes);
    }


}
