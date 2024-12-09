package com.example.order_sales.repository;

import com.example.order_sales.entity.Order;
import com.example.order_sales.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    OrderTransaction findFirstByOrderOrderByChangedAtDesc(Order order);
}
