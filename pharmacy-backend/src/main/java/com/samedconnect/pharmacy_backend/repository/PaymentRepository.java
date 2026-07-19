package com.samedconnect.pharmacy_backend.repository;

import com.samedconnect.pharmacy_backend.entity.Order;
import com.samedconnect.pharmacy_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(String paymentReference);

    Optional<Payment> findByOrderId(Long orderId);
    boolean existsByOrder(Order order);

}