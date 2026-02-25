package com.example.Tiffin_Management.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardSummaryDTO {
    private Long totalUsers;
    private Long totalOrders;
    private Long todayOrders;
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private String mostOrderedDish;
}
