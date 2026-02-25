package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.response.DailyOrderCountDTO;
import com.example.Tiffin_Management.dto.response.DashboardSummaryDTO;
import com.example.Tiffin_Management.dto.response.MonthlyRevenueDTO;
import com.example.Tiffin_Management.dto.response.TopCustomerDTO;
import com.example.Tiffin_Management.repository.OrderItemRepository;
import com.example.Tiffin_Management.repository.OrderRepository;
import com.example.Tiffin_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public DashboardSummaryDTO getDashboardSummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();
        Long todayOrders = orderRepository.countOrdersBetweenDates(startOfDay, endOfDay);

        BigDecimal totalRevenue = orderRepository.getTotalRevenue();
        BigDecimal todayRevenue = orderRepository.getRevenueBetweenDates(startOfDay, endOfDay);

        String mostOrderedDish = orderItemRepository.findMostOrderedDish();

        return DashboardSummaryDTO.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .todayOrders(todayOrders != null ? todayOrders : 0L)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .todayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO)
                .mostOrderedDish(mostOrderedDish)
                .build();
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenue() {
        List<Object[]> results = orderRepository.getMonthlyRevenue();
        List<MonthlyRevenueDTO> response = new ArrayList<>();
        for (Object[] row : results) {
            String month = row[0].toString();
            BigDecimal revenue = row[1] instanceof BigDecimal ? (BigDecimal) row[1] : new BigDecimal(row[1].toString());
            response.add(new MonthlyRevenueDTO(month, revenue));
        }
        return response;
    }

    public List<DailyOrderCountDTO> getDailyOrders(int days) {
        LocalDateTime startDate = LocalDate.now().minusDays(days).atStartOfDay();
        List<Object[]> results = orderRepository.getDailyOrders(startDate);

        List<DailyOrderCountDTO> response = new ArrayList<>();
        for (Object[] row : results) {
            Object dateObj = row[0];
            LocalDate date;
            if (dateObj instanceof java.sql.Date) {
                date = ((java.sql.Date) dateObj).toLocalDate();
            } else if (dateObj instanceof java.sql.Timestamp) {
                date = ((java.sql.Timestamp) dateObj).toLocalDateTime().toLocalDate();
            } else {
                date = LocalDate.parse(dateObj.toString());
            }
            Long count = ((Number) row[1]).longValue();
            response.add(new DailyOrderCountDTO(date, count));
        }
        return response;
    }

    public TopCustomerDTO getTopCustomer() {
        List<Object[]> results = orderRepository.getTopCustomers(1);
        if (results != null && !results.isEmpty()) {
            Object[] row = results.get(0);
            String userName = row[0].toString();
            Long totalOrders = ((Number) row[1]).longValue();
            BigDecimal totalSpent = row[2] instanceof BigDecimal ? (BigDecimal) row[2]
                    : new BigDecimal(row[2].toString());
            return new TopCustomerDTO(userName, totalOrders, totalSpent);
        }
        return null;
    }
}
