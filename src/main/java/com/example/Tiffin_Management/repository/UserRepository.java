package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByShop_Id(Long shopId, Pageable pageable);

    Optional<User> findByIdAndShop_Id(Long id, Long shopId);

    Long countByShop_Id(Long shopId);
}
