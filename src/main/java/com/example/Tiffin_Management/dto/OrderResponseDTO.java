package com.example.Tiffin_Management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponseDTO> items;
}
