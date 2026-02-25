package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    BigDecimal getRevenueBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    Long countOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT TO_CHAR(o.order_date, 'YYYY-MM') AS month, SUM(o.total_amount) AS revenue " +
            "FROM order_table o " +
            "GROUP BY TO_CHAR(o.order_date, 'YYYY-MM') " +
            "ORDER BY month DESC", nativeQuery = true)
    List<Object[]> getMonthlyRevenue();

    @Query(value = "SELECT DATE(o.order_date) AS date, COUNT(o.id) AS orderCount " +
            "FROM order_table o " +
            "WHERE o.order_date >= :startDate " +
            "GROUP BY DATE(o.order_date) " +
            "ORDER BY date DESC", nativeQuery = true)
    List<Object[]> getDailyOrders(@Param("startDate") LocalDateTime startDate);

    @Query(value = "SELECT u.name AS userName, COUNT(o.id) AS totalOrders, SUM(o.total_amount) AS totalSpent " +
            "FROM order_table o " +
            "JOIN user_table u ON o.user_id = u.id " +
            "GROUP BY u.id, u.name " +
            "ORDER BY totalSpent DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> getTopCustomers(@Param("limit") int limit);
}
