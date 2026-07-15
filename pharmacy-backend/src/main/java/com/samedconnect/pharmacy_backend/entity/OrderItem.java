package com.samedconnect.pharmacy_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which order does this item belong to?
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Which medicine was ordered?
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // Quantity ordered
    @Column(nullable = false)
    private Integer quantity;

    // Price of the medicine at the time of ordering
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}