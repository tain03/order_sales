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
        // Kiểm tra nếu không có customerId, tự tạo hoặc tra cứu khách hàng mới
        Customer customer = customerService.findOrCreateCustomer(
                null,  // customerId là null nếu không có trong request
                orderDTO.getCustomer().getCustomerName(),
                orderDTO.getCustomer().getCustomerEmail(),
                orderDTO.getCustomer().getCustomerPhone(),
                orderDTO.getCustomer().getShippingAddress()
        );

        Order newOrder = orderService.createOrder(customer, orderDTO);

        // Chuyển đổi các item trong đơn hàng thành DTO
        List<OrderItemDTO> orderItemDTOs = newOrder.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getProductId(),
                        item.getProduct().getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        // Chuyển đổi thông tin đơn hàng thành DTO
        OrderDetailsDTO orderDetails = new OrderDetailsDTO(
                newOrder.getOrderId(),
                newOrder.getOrderDate(),
                newOrder.getTotalAmount(),
                newOrder.getShippingMethod(),
                newOrder.getPaymentMethod(),
                newOrder.getNotes()
        );

        // Chuyển đổi thông tin trả về
        OrderDTO response = new OrderDTO();
        response.setCustomer(new CustomerDTO(
//                customer.getCustomerId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
        ));
        response.setOrder(orderDetails);
        response.setOrderItems(orderItemDTOs);

        // Gửi email xác nhận đơn hàng
        String orderDetailsText = generateOrderDetails(newOrder);
        emailService.sendOrderConfirmationEmail(customer.getEmail(), orderDetailsText);

        return response;
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order and log the transaction")
    @PutMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long orderId,
                                  @RequestParam OrderStatus newStatus,
                                  @RequestParam(required = false) String notes) {
        // Call the service to update the order status and log the transaction
        orderService.updateOrderStatus(orderId, newStatus, notes);
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
