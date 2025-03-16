package com.example.school.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Component;

@Component
public class DynamicSchemaUpdater {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public void updateSchema() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.createNativeQuery("ALTER TABLE Student ADD COLUMN IF NOT EXISTS extraField VARCHAR(255)").executeUpdate();
        } finally {
            entityManager.close();
        }
    }
}
