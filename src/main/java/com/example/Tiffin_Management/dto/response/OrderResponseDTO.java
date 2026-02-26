package com.example.Tiffin_Management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private String status;
    private List<OrderItemResponseDTO> items;
}
