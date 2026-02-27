package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        @Query("SELECT SUM(o.totalAmount) FROM Order o")
        BigDecimal getTotalRevenue();

        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate = :date")
        BigDecimal getDailyRevenue(@Param("date") LocalDate date);

        @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = :date")
        Long countByOrderDate(@Param("date") LocalDate date);

        @Query(value = "SELECT SUM(total_amount) FROM orders WHERE strftime('%Y-%m', order_date) = :yearMonth", nativeQuery = true)
        BigDecimal getMonthlyRevenue(@Param("yearMonth") String yearMonth);

        @Query(value = "SELECT COUNT(*) FROM orders WHERE strftime('%Y-%m', order_date) = :yearMonth", nativeQuery = true)
        Long getMonthlyOrderCount(@Param("yearMonth") String yearMonth);

        @Query(value = "SELECT DATE(o.order_date) AS date, COUNT(o.id) AS orderCount " +
                        "FROM orders o " +
                        "WHERE o.order_date >= :startDate AND o.order_date IS NOT NULL " +
                        "GROUP BY DATE(o.order_date) " +
                        "ORDER BY date DESC", nativeQuery = true)
        List<Object[]> getDailyOrders(@Param("startDate") LocalDate startDate);

        @Query(value = "SELECT u.name AS userName, COUNT(o.id) AS totalOrders, SUM(o.total_amount) AS totalSpent " +
                        "FROM orders o " +
                        "JOIN customers u ON o.user_id = u.id " +
                        "GROUP BY u.id, u.name " +
                        "ORDER BY totalSpent DESC " +
                        "LIMIT :limit", nativeQuery = true)
        List<Object[]> getTopCustomers(@Param("limit") int limit);
}
