package com.example.Tiffin_Management.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private List<OrderItemRequestDTO> items;
}
