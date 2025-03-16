package com.example.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant {
    //Role-Based Endpoint Restrictions

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Store hashed password (BCrypt)

    @Column(nullable = false, unique = true)
    private String databaseName; // H2 file-based database

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tenant_roles", joinColumns = @JoinColumn(name = "tenant_id"))
    @Column(name = "role")
    private Set<String> roles; // Defines allowed endpoints

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tenant_services", joinColumns = @JoinColumn(name = "tenant_id"))
    @Column(name = "service")
    private Set<String> allowedServices; // Defines accessible services

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getAllowedServices() {
        return allowedServices;
    }

    public void setAllowedServices(Set<String> allowedServices) {
        this.allowedServices = allowedServices;
    }
}
