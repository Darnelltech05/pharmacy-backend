package com.samedconnect.pharmacy_backend.controller;

import com.samedconnect.pharmacy_backend.dto.request.CreatePaymentRequest;
import com.samedconnect.pharmacy_backend.dto.response.PaymentResponse;
import com.samedconnect.pharmacy_backend.service.PaymentService;
import com.samedconnect.pharmacy_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Response<PaymentResponse>> makePayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = paymentService.makePayment(request);

        return ResponseEntity.ok(
                Response.success("Payment completed successfully", response)
        );
    }

    @GetMapping
    public ResponseEntity<Response<List<PaymentResponse>>> getAllPayments() {

        return ResponseEntity.ok(
                Response.success(
                        "Payments retrieved successfully",
                        paymentService.getAllPayments()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PaymentResponse>> getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Response.success(
                        "Payment retrieved successfully",
                        paymentService.getPaymentById(id)
                )
        );
    }
}