package com.koha.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {

    private static EntityManagerFactory factory;

    public static synchronized EntityManager getEntityManager() {
        if (factory == null || !factory.isOpen()) {
            try {
                factory = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
            } catch (Exception e) {
                try {
                    factory = Persistence.createEntityManagerFactory("dataSource");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("Không thể khởi tạo EntityManagerFactory từ persistence.xml", ex);
                }
            }
        }
        return factory.createEntityManager();
    }

    public static synchronized void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}

