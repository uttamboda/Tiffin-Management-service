package com.example.Tiffin_Management.repository;

import com.example.Tiffin_Management.entity.TenantShopMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TenantShopMapRepository extends JpaRepository<TenantShopMap, Long> {
    Optional<TenantShopMap> findByTenantId(Long tenantId);
}
