package com.example.school.service;


import com.example.school.model.Tenant;
import com.example.school.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TenantService {


    @Autowired
    private TenantRepository tenantRepository;

    public Tenant registerTenant(String collegeName) {
        Tenant tenant = new Tenant();
        tenant.setUsername(collegeName);
        tenant.setDatabaseName("tenant_db_" + collegeName);
        return tenantRepository.save(tenant);
    }

    public Tenant getTenantByUsername(String username) {
        return tenantRepository.getByTenantName(username)
                .orElseThrow(() -> new RuntimeException("Tenant not found with username: " + username));
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

}
