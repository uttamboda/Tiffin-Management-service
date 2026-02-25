package com.example.Tiffin_Management.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemResponseDTO {
    private Long id;
    private String dishName;
    private BigDecimal priceDefault;
}
