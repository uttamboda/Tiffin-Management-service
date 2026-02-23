package com.example.Tiffin_Management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuItem menu;

    @Column(name = "quantiity") // User specified typo
    private Integer quantity;

    @Column(name = "seallingprice") // User specified typo
    private BigDecimal sellingPrice;

    @Column(name = "item_subtotal")
    private BigDecimal itemSubtotal;
}
