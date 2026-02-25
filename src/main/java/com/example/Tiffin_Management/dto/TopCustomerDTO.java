package com.example.Tiffin_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCustomerDTO {
    private String userName;
    private Long totalOrders;
    private BigDecimal totalSpent;
}
