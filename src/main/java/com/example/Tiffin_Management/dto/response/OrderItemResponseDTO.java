package com.example.Tiffin_Management.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long id;
    private Long menuId;
    private String dishName;
    private Integer quantity;
    private BigDecimal sellingPrice;
    private BigDecimal itemSubtotal;
}
