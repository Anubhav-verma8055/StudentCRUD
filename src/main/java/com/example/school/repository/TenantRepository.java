package com.example.school.repository;

import com.example.school.model.Fees;
import com.example.school.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> getByTenantName(String username);

    Optional<Tenant>  findByUsername(String username);
}
