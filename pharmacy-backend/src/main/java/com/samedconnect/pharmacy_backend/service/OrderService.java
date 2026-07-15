package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderItemRequest;
import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.request.UpdateOrderStatusRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderItemResponse;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.OrderItem;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.exception.BadRequestException;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
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

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {

        User customer = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = new Order();

        order.setUser(customer);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPickupLocation(request.getPickupLocation());
        order.setTotalAmount(BigDecimal.ZERO);

        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : request.getItems()) {

            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Medicine not found"));

            if (medicine.getStockQuantity() < itemRequest.getQuantity()) {
                throw new BadRequestException(
                        medicine.getName() + " has insufficient stock");
            }

            medicine.setStockQuantity(
                    medicine.getStockQuantity() - itemRequest.getQuantity());

            medicineRepository.save(medicine);

            BigDecimal subtotal = medicine.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            total = total.add(subtotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(medicine.getPrice());

            orderItemRepository.save(orderItem);

            itemResponses.add(
                    OrderItemResponse.builder()
                            .medicineId(medicine.getId())
                            .medicineName(medicine.getName())
                            .quantity(itemRequest.getQuantity())
                            .price(medicine.getPrice())
                            .subtotal(subtotal)
                            .build()
            );
        }

        order.setTotalAmount(total);

        orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(customer.getFullName())
                .status(order.getStatus().name())
                .totalAmount(total)
                .pickupLocation(order.getPickupLocation())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {

        User customer = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(customer);

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            responses.add(buildOrderResponse(order));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {

        User customer = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(customer.getId())) {
            throw new BadRequestException("You are not allowed to view this order.");
        }

        return buildOrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        try {
            order.setStatus(
                    Order.OrderStatus.valueOf(
                            request.getStatus().toUpperCase()
                    )
            );
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid order status.");
        }

        orderRepository.save(order);

        return buildOrderResponse(order);
    }

    private OrderResponse buildOrderResponse(Order order) {

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : items) {

            BigDecimal subtotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            itemResponses.add(
                    OrderItemResponse.builder()
                            .medicineId(item.getMedicine().getId())
                            .medicineName(item.getMedicine().getName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .subtotal(subtotal)
                            .build()
            );
        }

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getUser().getFullName())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .pickupLocation(order.getPickupLocation())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }
}