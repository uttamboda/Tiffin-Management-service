package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByShop_Id(Long shopId);

    Optional<MenuItem> findByIdAndShop_Id(Long id, Long shopId);
}
