package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.dto.response.DailyOrderCountDTO;
import com.example.Tiffin_Management.dto.response.DashboardSummaryDTO;
import com.example.Tiffin_Management.dto.response.MonthlyRevenueDTO;
import com.example.Tiffin_Management.dto.response.TopCustomerDTO;
import com.example.Tiffin_Management.repository.OrderItemRepository;
import com.example.Tiffin_Management.repository.OrderRepository;
import com.example.Tiffin_Management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getDashboardSummary_ReturnsData() {
        when(userRepository.count()).thenReturn(100L);
        when(orderRepository.count()).thenReturn(500L);
        when(orderRepository.countByOrderDate(any(LocalDate.class)))
                .thenReturn(20L);
        when(orderRepository.getTotalRevenue()).thenReturn(new BigDecimal("50000.00"));
        when(orderRepository.getDailyRevenue(any(LocalDate.class)))
                .thenReturn(new BigDecimal("2000.00"));
        when(orderItemRepository.findMostOrderedDish()).thenReturn("Chicken Tikka");

        DashboardSummaryDTO summary = analyticsService.getDashboardSummary();

        assertNotNull(summary);
        assertEquals(100L, summary.getTotalUsers());
        assertEquals(500L, summary.getTotalOrders());
        assertEquals(20L, summary.getTodayOrders());
        assertEquals(new BigDecimal("50000.00"), summary.getTotalRevenue());
        assertEquals(new BigDecimal("2000.00"), summary.getTodayRevenue());
        assertEquals("Chicken Tikka", summary.getMostOrderedDish());

        verify(userRepository, times(1)).count();
    }

    @Test
    void getMonthlyRevenue_ReturnsData() {
        when(orderRepository.getMonthlyRevenue(anyString())).thenReturn(new BigDecimal("15000.00"));

        List<MonthlyRevenueDTO> revenueList = analyticsService.getMonthlyRevenue();

        assertNotNull(revenueList);
        assertEquals(6, revenueList.size());
        assertEquals(new BigDecimal("15000.00"), revenueList.get(0).getRevenue());
    }

    @Test
    void getDailyOrders_ReturnsData() {
        Object[] row = { java.sql.Date.valueOf(LocalDate.now()), 15L };
        List<Object[]> queryResult = java.util.Collections.singletonList(row);

        when(orderRepository.getDailyOrders(any(LocalDate.class))).thenReturn(queryResult);

        List<DailyOrderCountDTO> dailyOrders = analyticsService.getDailyOrders(30);

        assertNotNull(dailyOrders);
        assertEquals(1, dailyOrders.size());
        assertEquals(LocalDate.now(), dailyOrders.get(0).getDate());
        assertEquals(15L, dailyOrders.get(0).getOrderCount());
    }

    @Test
    void getTopCustomer_ReturnsData() {
        Object[] row = { "Test User", 50L, new BigDecimal("10000.00") };
        List<Object[]> queryResult = java.util.Collections.singletonList(row);

        when(orderRepository.getTopCustomers(1)).thenReturn(queryResult);

        TopCustomerDTO topCustomer = analyticsService.getTopCustomer();

        assertNotNull(topCustomer);
        assertEquals("Test User", topCustomer.getUserName());
        assertEquals(50L, topCustomer.getTotalOrders());
        assertEquals(new BigDecimal("10000.00"), topCustomer.getTotalSpent());
    }
}
