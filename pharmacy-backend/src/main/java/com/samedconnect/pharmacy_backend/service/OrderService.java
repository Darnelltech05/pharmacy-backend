package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreateOrderRequest;
import com.samedconnect.pharmacy_backend.dto.response.OrderResponse;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.OrderItem;
import com.samedconnect.pharmacy_backend.entity.Medicine;
import com.samedconnect.pharmacy_backend.entity.User;
import com.samedconnect.pharmacy_backend.exception.BadRequestException;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.repository.OrderRepository;
import com.samedconnect.pharmacy_backend.repository.MedicineRepository;
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
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderUuid(UUID.randomUUID().toString());
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(request.getShippingAddress());
        order.setClinicPickupLocation(request.getClinicPickupLocation());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;  // ✅ Use BigDecimal for money

        // Process each item
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Medicine not found with id: " + itemRequest.getMedicineId()));

            // Check stock
            if (medicine.getStockQuantity() < itemRequest.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for medicine: " + medicine.getName() +
                                ". Available: " + medicine.getStockQuantity() +
                                ", Requested: " + itemRequest.getQuantity());
            }

            // Reduce stock
            medicine.setStockQuantity(medicine.getStockQuantity() - itemRequest.getQuantity());
            medicineRepository.save(medicine);

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(itemRequest.getQuantity());

            // ✅ Use BigDecimal for price calculations
            BigDecimal priceAtTime = medicine.getPrice();
            orderItem.setPriceAtTime(priceAtTime);

            // ✅ Calculate subtotal using BigDecimal
            BigDecimal subtotal = priceAtTime.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);

            // ✅ Add to total
            totalAmount = totalAmount.add(subtotal);
        }

        order.setOrderItems(orderItems);

        // ✅ Convert BigDecimal to Double for storage
        order.setTotalAmount(totalAmount.doubleValue());

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderUuid(order.getOrderUuid());
        response.setUserId(order.getUser().getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setClinicPickupLocation(order.getClinicPickupLocation());

        // Map order items
        List<OrderResponse.OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setMedicineId(item.getMedicine().getId());
            itemResponse.setMedicineName(item.getMedicine().getName());
            itemResponse.setQuantity(item.getQuantity());

            //  Handle BigDecimal to Double conversion
            if (item.getPriceAtTime() != null) {
                itemResponse.setPriceAtTime(item.getPriceAtTime().doubleValue());
            }
            if (item.getSubtotal() != null) {
                itemResponse.setSubtotal(item.getSubtotal().doubleValue());
            }

            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);

        return response;
    }
}