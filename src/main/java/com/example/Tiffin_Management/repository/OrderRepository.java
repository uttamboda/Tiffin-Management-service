package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        Page<Order> findAllByShop_Id(Long shopId, Pageable pageable);

        Optional<Order> findByIdAndShop_Id(Long id, Long shopId);

        Long countByShop_Id(Long shopId);

        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.shop.id = :shopId")
        BigDecimal getTotalRevenue(@Param("shopId") Long shopId);

        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate = :date AND o.shop.id = :shopId")
        BigDecimal getDailyRevenue(@Param("date") LocalDate date, @Param("shopId") Long shopId);

        @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate = :date AND o.shop.id = :shopId")
        Long countByOrderDateAndShop_Id(@Param("date") LocalDate date, @Param("shopId") Long shopId);

        @Query(value = "SELECT SUM(total_amount) FROM orders WHERE strftime('%Y-%m', order_date) = :yearMonth AND shop_id = :shopId", nativeQuery = true)
        BigDecimal getMonthlyRevenue(@Param("yearMonth") String yearMonth, @Param("shopId") Long shopId);

        @Query(value = "SELECT COUNT(*) FROM orders WHERE strftime('%Y-%m', order_date) = :yearMonth AND shop_id = :shopId", nativeQuery = true)
        Long getMonthlyOrderCount(@Param("yearMonth") String yearMonth, @Param("shopId") Long shopId);

        @Query(value = "SELECT DATE(o.order_date) AS date, COUNT(o.id) AS orderCount " +
                        "FROM orders o " +
                        "WHERE o.order_date >= :startDate AND o.order_date IS NOT NULL AND o.shop_id = :shopId " +
                        "GROUP BY DATE(o.order_date) " +
                        "ORDER BY date DESC", nativeQuery = true)
        List<Object[]> getDailyOrders(@Param("startDate") LocalDate startDate, @Param("shopId") Long shopId);

        @Query(value = "SELECT u.name AS userName, COUNT(o.id) AS totalOrders, SUM(o.total_amount) AS totalSpent " +
                        "FROM orders o " +
                        "JOIN customers u ON o.user_id = u.id " +
                        "WHERE o.shop_id = :shopId " +
                        "GROUP BY u.id, u.name " +
                        "ORDER BY totalSpent DESC " +
                        "LIMIT :limit", nativeQuery = true)
        List<Object[]> getTopCustomers(@Param("limit") int limit, @Param("shopId") Long shopId);
}
