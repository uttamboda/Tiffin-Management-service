package com.example.Tiffin_Management.controller;

import com.example.Tiffin_Management.dto.DailyOrderCountDTO;
import com.example.Tiffin_Management.dto.DashboardSummaryDTO;
import com.example.Tiffin_Management.dto.MonthlyRevenueDTO;
import com.example.Tiffin_Management.dto.TopCustomerDTO;
import com.example.Tiffin_Management.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(analyticsService.getDashboardSummary());
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<MonthlyRevenueDTO>> getMonthlyRevenue() {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenue());
    }

    @GetMapping("/orders/daily")
    public ResponseEntity<List<DailyOrderCountDTO>> getDailyOrders(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(analyticsService.getDailyOrders(days));
    }

    @GetMapping("/users/top")
    public ResponseEntity<TopCustomerDTO> getTopCustomer() {
        TopCustomerDTO topCustomer = analyticsService.getTopCustomer();
        if (topCustomer == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topCustomer);
    }
}
