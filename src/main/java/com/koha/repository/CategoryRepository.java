package com.koha.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.koha.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByOrderByCategoryIdDesc();

    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryname) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY c.categoryId DESC")
    List<Category> searchByName(@Param("keyword") String keyword);

    boolean existsByCategoryname(String categoryname);
}

