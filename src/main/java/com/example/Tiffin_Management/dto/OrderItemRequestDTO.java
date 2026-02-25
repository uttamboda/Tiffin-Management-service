package com.example.Tiffin_Management.dto;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long menuId;
    private Integer quantity;
    private java.math.BigDecimal sellingPrice;
}
