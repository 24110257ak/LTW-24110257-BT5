package com.koha.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import com.koha.config.JpaConfig;
import com.koha.dao.IProductDao;
import com.koha.entity.Product;

public class ProductDao implements IProductDao {

    @Override
    public void insert(Product product) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(product);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(product);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(int productId) throws Exception {
        EntityManager enma = JpaConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Product product = enma.find(Product.class, productId);
            if (product != null) {
                enma.remove(product);
            } else {
                throw new Exception("Không tìm thấy sản phẩm có ID = " + productId);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public Product findById(int productId) {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            return enma.find(Product.class, productId);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager enma = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = enma.createNamedQuery("Product.findAll", Product.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> searchByName(String productName) {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT p FROM Product p WHERE p.productName LIKE :keyword ORDER BY p.productId DESC";
        try {
            TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
            query.setParameter("keyword", "%" + productName + "%");
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findTop10() {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.productId DESC";
        try {
            TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
            query.setMaxResults(10);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findAll(int page, int pageSize) {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.productId DESC";
        try {
            TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT COUNT(p) FROM Product p WHERE p.status = 1";
        try {
            Query query = enma.createQuery(jpql);
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        EntityManager enma = JpaConfig.getEntityManager();
        String jpql = "SELECT p FROM Product p WHERE p.category.categoryId = :cateId AND p.status = 1 ORDER BY p.productId DESC";
        try {
            TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
            query.setParameter("cateId", categoryId);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }
}
