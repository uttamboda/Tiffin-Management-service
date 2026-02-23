package com.example.Tiffin_Management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_name")
    private String dishName;

    @Column(name = "price_defulat") // Deliberate typo matching database exactly
    private BigDecimal priceDefault;
}
