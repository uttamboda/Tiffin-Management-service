package com.example.Tiffin_Management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {
    @NotNull(message = "Menu ID cannot be null")
    private Long menuId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Min(value = 0, message = "Selling price cannot be negative")
    private java.math.BigDecimal sellingPrice;
}
