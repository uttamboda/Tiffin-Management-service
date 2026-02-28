package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.response.DailyOrderCountDTO;
import com.example.Tiffin_Management.dto.response.DashboardSummaryDTO;
import com.example.Tiffin_Management.dto.response.MonthlyRevenueDTO;
import com.example.Tiffin_Management.dto.response.TopCustomerDTO;
import com.example.Tiffin_Management.repository.OrderItemRepository;
import com.example.Tiffin_Management.repository.OrderRepository;
import com.example.Tiffin_Management.repository.UserRepository;
import com.example.Tiffin_Management.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ShopService shopService;

    public DashboardSummaryDTO getDashboardSummary() {
        Long shopId = shopService.getCurrentShop().getId();
        LocalDate today = LocalDate.now();

        Long totalUsers = userRepository.countByShop_Id(shopId);
        Long totalOrders = orderRepository.countByShop_Id(shopId);
        Long todayOrders = orderRepository.countByOrderDateAndShop_Id(today, shopId);

        BigDecimal totalRevenue = orderRepository.getTotalRevenue(shopId);
        BigDecimal todayRevenue = orderRepository.getDailyRevenue(today, shopId);

        String mostOrderedDish = orderItemRepository.findMostOrderedDish(shopId);

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
        Long shopId = shopService.getCurrentShop().getId();
        List<MonthlyRevenueDTO> response = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        for (int i = 0; i < 6; i++) {
            String yearMonthStr = currentMonth.minusMonths(i).toString();
            BigDecimal revenue = orderRepository.getMonthlyRevenue(yearMonthStr, shopId);
            response.add(new MonthlyRevenueDTO(yearMonthStr, revenue != null ? revenue : BigDecimal.ZERO));
        }
        return response;
    }

    public List<DailyOrderCountDTO> getDailyOrders(int days) {
        Long shopId = shopService.getCurrentShop().getId();
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<Object[]> results = orderRepository.getDailyOrders(startDate, shopId);

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
        Long shopId = shopService.getCurrentShop().getId();
        List<Object[]> results = orderRepository.getTopCustomers(1, shopId);
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
