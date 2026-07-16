package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.request.OrderItemRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.OrderItem;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.repository.MedicineRepository;
import com.samedconnect.pharmacy_backend.repository.OrderItemRepository;
import com.samedconnect.pharmacy_backend.repository.OrderRepository;
import com.samedconnect.pharmacy_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setOrderUuid(UUID.randomUUID().toString());
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setClinicPickupLocation(request.getClinicPickupLocation());
        order.setStatus(Order.OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtTime(medicine.getPrice());

            orderItemRepository.save(orderItem);

            orderItems.add(orderItem);

            total = total.add(
                    medicine.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
            );
        }

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalAmount(total);

        orderRepository.save(savedOrder);

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .orderUuid(savedOrder.getOrderUuid())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus().name())
                .shippingAddress(savedOrder.getShippingAddress())
                .clinicPickupLocation(savedOrder.getClinicPickupLocation())
                .orderDate(savedOrder.getOrderDate())
                .build();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }
}