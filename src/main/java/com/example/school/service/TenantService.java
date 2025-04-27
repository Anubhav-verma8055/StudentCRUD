package com.example.school.service;


import com.example.school.model.Tenant;
import com.example.school.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {


    @Autowired
    private TenantRepository tenantRepository;

    public Tenant registerTenant(Tenant tenantRequest) {
        Tenant tenant = new Tenant();
        tenant.setUsername(tenantRequest.getUsername());
        tenant.setPassword(tenantRequest.getPassword());
        tenant.setAllowedServices(tenantRequest.getAllowedServices());
        tenant.setRoles(tenantRequest.getRoles());
        tenant.setCollegeName(tenantRequest.getCollegeName());
        tenant.setDatabaseName("tenant_db_" + tenant.getCollegeName());
        return tenantRepository.save(tenant);
    }

    public Tenant getTenantByUsername(String username) {
        return tenantRepository.getByTenantName(username)
                .orElseThrow(() -> new RuntimeException("Tenant not found with username: " + username));
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(Long id) {
        Optional<Tenant> tenant = tenantRepository.findById(id);
        return tenant.orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
    }

    public Tenant updateTenant(Long id, String userName) {
        //add other esential to keep the properties assocaited with tenant object
        Tenant tenant = getTenantById(id);
        tenant.setUsername(userName);
        tenant.setDatabaseName("tenant_db_" + userName);
        return tenantRepository.save(tenant);
    }

    public void deleteTenant(Long id) {
        Tenant tenant = getTenantById(id);
        tenantRepository.delete(tenant);
    }



}
