package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreatePaymentRequest;
import com.samedconnect.pharmacy_backend.dto.response.PaymentResponse;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.Payment;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.repository.OrderRepository;
import com.samedconnect.pharmacy_backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + request.getOrderId()));

        if (paymentRepository.existsByOrder(order)) {
            throw new IllegalArgumentException("Payment already exists for this order.");
        }

        Payment payment = Payment.builder()
                .paymentReference(UUID.randomUUID().toString())
                .order(order)
                .amount(java.math.BigDecimal.valueOf(order.getTotalAmount()))
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(Payment.PaymentStatus.SUCCESSFUL)
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found with id: " + id));

        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for order id: " + orderId));

        return mapToResponse(payment);
    }
    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, Payment.PaymentStatus status) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found with id: " + id));

        payment.setPaymentStatus(status);

        Payment updatedPayment = paymentRepository.save(payment);

        return mapToResponse(updatedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found with id: " + id));

        paymentRepository.delete(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setPaymentReference(payment.getPaymentReference());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }
}