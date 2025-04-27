package com.example.school.controller;

import com.example.school.model.Tenant;
import com.example.school.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    // Create/Register a new tenant
    @PostMapping("/register")
    public ResponseEntity<Tenant> registerTenant(@RequestBody Tenant tenantRequest) {
        Tenant tenant = tenantService.registerTenant(tenantRequest);
        return ResponseEntity.ok(tenant);
    }

    // Get all tenants
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')") // Only admins can list all tenants
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    // Get tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) {
        Tenant tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(tenant);
    }

    // Update tenant
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @RequestParam String userName) {
        Tenant updatedTenant = tenantService.updateTenant(id, userName);
        return ResponseEntity.ok(updatedTenant);
    }

    // Delete tenant
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')") // Only admins can delete tenants
    public ResponseEntity<String> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.ok("Tenant deleted successfully");
    }
}
