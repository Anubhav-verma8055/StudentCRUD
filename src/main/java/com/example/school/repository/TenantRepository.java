package com.example.school.repository;

import com.example.school.model.Fees;
import com.example.school.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> getByTenantName(String username);
}
