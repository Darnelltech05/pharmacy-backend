package com.samedconnect.pharmacy_backend.service;

import com.samedconnect.pharmacy_backend.dto.request.CreatePaymentRequest;
import com.samedconnect.pharmacy_backend.dto.response.PaymentResponse;
import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.Payment;
import com.samedconnect.pharmacy_backend.exception.BadRequestException;
import com.samedconnect.pharmacy_backend.exception.ResourceNotFoundException;
import com.samedconnect.pharmacy_backend.repository.OrderRepository;
import com.samedconnect.pharmacy_backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse makePayment(CreatePaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (paymentRepository.existsByOrder(order)) {
            throw new BadRequestException("This order has already been paid.");
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());

        payment.setPaymentMethod(
                Payment.PaymentMethod.valueOf(
                        request.getPaymentMethod().toUpperCase()
                )
        );

        payment = paymentRepository.save(payment);

        return mapToResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentUuid(payment.getPaymentUuid())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod().name())
                .status(payment.getStatus().name())
                .transactionReference(payment.getTransactionReference())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}