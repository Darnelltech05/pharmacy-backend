package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.request.OrderItemRequest;
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
import java.time.LocalDateTime;
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
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setOrderUuid(UUID.randomUUID().toString());
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setClinicPickupLocation(request.getClinicPickupLocation());
        order.setPrescriptionUrl(request.getPrescriptionUrl());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + itemRequest.getMedicineId()));

            if (medicine.getIsArchived()) {
                throw new BadRequestException("Medicine is no longer available: " + medicine.getName());
            }

            if (medicine.getRequiresPrescription() && (request.getPrescriptionUrl() == null || request.getPrescriptionUrl().isBlank())) {
                throw new BadRequestException("Prescription is required for medicine: " + medicine.getName());
            }

            if (medicine.getStockQuantity() < itemRequest.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for medicine: " + medicine.getName() +
                                ". Available: " + medicine.getStockQuantity() +
                                ", Requested: " + itemRequest.getQuantity()
                );
            }

            medicine.setStockQuantity(medicine.getStockQuantity() - itemRequest.getQuantity());
            medicineRepository.save(medicine);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtTime(medicine.getPrice());
            orderItem.setCreatedAt(LocalDateTime.now());

            BigDecimal subtotal = medicine.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItemRepository.save(orderItem);

            orderItems.add(orderItem);
            total = total.add(subtotal);

            OrderItemResponse itemResponse = OrderItemResponse.builder()
                    .id(orderItem.getId())
                    .medicineId(medicine.getId())
                    .medicineName(medicine.getName())
                    .quantity(orderItem.getQuantity())
                    .priceAtTime(orderItem.getPriceAtTime())
                    .subtotal(orderItem.getSubtotal())
                    .build();
            orderItemResponses.add(itemResponse);
        }

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalAmount(total);
        savedOrder.setUpdatedAt(LocalDateTime.now());

        Order finalOrder = orderRepository.save(savedOrder);

        return OrderResponse.builder()
                .id(finalOrder.getId())
                .orderUuid(finalOrder.getOrderUuid())
                .totalAmount(finalOrder.getTotalAmount())
                .status(finalOrder.getStatus().name())
                .shippingAddress(finalOrder.getShippingAddress())
                .clinicPickupLocation(finalOrder.getClinicPickupLocation())
                .prescriptionUrl(finalOrder.getPrescriptionUrl())
                .orderDate(finalOrder.getOrderDate())
                .items(orderItemResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems() != null
                ? order.getOrderItems().stream().map(this::toItemResponse).toList()
                : new ArrayList<>();

        return OrderResponse.builder()
                .id(order.getId())
                .orderUuid(order.getOrderUuid())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .shippingAddress(order.getShippingAddress())
                .clinicPickupLocation(order.getClinicPickupLocation())
                .prescriptionUrl(order.getPrescriptionUrl())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .medicineId(item.getMedicine().getId())
                .medicineName(item.getMedicine().getName())
                .quantity(item.getQuantity())
                .priceAtTime(item.getPriceAtTime())
                .subtotal(item.getSubtotal())
                .build();
    }
}