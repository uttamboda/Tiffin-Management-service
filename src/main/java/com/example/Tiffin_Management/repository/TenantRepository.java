package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByUsername(String username);

    boolean existsByUsername(String username);
}
