package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);
}
