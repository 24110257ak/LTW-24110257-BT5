package com.koha.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.koha.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findTop10ByOrderByProductIdDesc();

    Page<Product> findAllByOrderByProductIdDesc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.productId DESC")
    List<Product> searchByName(@Param("keyword") String keyword);

    List<Product> findByCategory_CategoryId(int categoryId);
}

