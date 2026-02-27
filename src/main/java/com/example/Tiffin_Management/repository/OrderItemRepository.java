package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = "SELECT m.dish_name " +
            "FROM order_items oi " +
            "JOIN menu_items m ON oi.menu_id = m.id " +
            "GROUP BY m.id, m.dish_name " +
            "ORDER BY SUM(oi.quantiity) DESC " +
            "LIMIT 1", nativeQuery = true)
    String findMostOrderedDish();
}
