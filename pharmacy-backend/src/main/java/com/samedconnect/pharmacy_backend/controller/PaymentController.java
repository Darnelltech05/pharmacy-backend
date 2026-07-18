package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreatePaymentRequest;
import com.samedconnect.pharmacy_backend.dto.response.PaymentResponse;
import com.samedconnect.pharmacy_backend.entity.Payment;
import com.samedconnect.pharmacy_backend.service.PaymentService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse payment = paymentService.createPayment(request);

        return Response.success("Payment created successfully", payment);
    }

    @GetMapping
    public Response<List<PaymentResponse>> getAllPayments() {

        return Response.success(
                "Payments retrieved successfully",
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/{id}")
    public Response<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        return Response.success(
                "Payment retrieved successfully",
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/order/{orderId}")
    public Response<PaymentResponse> getPaymentByOrderId(
            @PathVariable Long orderId) {

        return Response.success(
                "Payment retrieved successfully",
                paymentService.getPaymentByOrderId(orderId)
        );
    }
    @PutMapping("/{id}/status")
    public Response<PaymentResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam Payment.PaymentStatus status) {

        return Response.success(
                "Payment status updated successfully",
                paymentService.updatePaymentStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public Response<Void> deletePayment(
            @PathVariable Long id) {

        paymentService.deletePayment(id);

        return Response.success(
                "Payment deleted successfully",
                null
        );
    }

}