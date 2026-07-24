package com.samedconnect.pharmacy_backend.repository;

import com.samedconnect.pharmacy_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}