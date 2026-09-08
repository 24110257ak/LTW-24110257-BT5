package com.koha.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koha.dao.ICategoryDao;
import com.koha.dao.impl.CategoryDao;
import com.koha.entity.Category;
import com.koha.repository.CategoryRepository;
import com.koha.service.ICategoryService;
import com.koha.util.Constant;

@Service
@Primary
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Autowired(required = false)
    private CategoryRepository categoryRepository;

    public ICategoryDao cateDao = new CategoryDao();

    public CategoryServiceImpl() {
    }

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        if (categoryRepository != null) {
            return categoryRepository.findAllByOrderByCategoryIdDesc();
        }
        return cateDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(int id) {
        if (categoryRepository != null) {
            return categoryRepository.findById(id).orElse(null);
        }
        return cateDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> searchByName(String keyword) {
        if (categoryRepository != null) {
            return categoryRepository.searchByName(keyword != null ? keyword.trim() : "");
        }
        return cateDao.searchByName(keyword);
    }

    @Override
    public void insert(Category category) {
        Category cate = this.findByCategoryname(category.getCategoryname());
        if (cate == null) {
            if (categoryRepository != null) {
                categoryRepository.save(category);
            } else {
                cateDao.insert(category);
            }
        }
    }

    @Override
    public void update(Category category) {
        Category cate = this.findById(category.getCategoryId());
        if (cate != null) {
            // Xóa file ảnh cũ nếu có ảnh mới upload
            if (category.getImages() != null && !category.getImages().equals(cate.getImages())) {
                String oldImage = cate.getImages();
                if (oldImage != null && !oldImage.startsWith("http")) {
                    File oldFile = new File(Constant.DIR + "/" + oldImage);
                    if (oldFile.exists() && oldFile.isFile()) {
                        oldFile.delete();
                    }
                }
            }
            if (categoryRepository != null) {
                categoryRepository.save(category);
            } else {
                cateDao.update(category);
            }
        }
    }

    @Override
    public void delete(int id) {
        try {
            Category cate = this.findById(id);
            if (cate != null) {
                String oldImage = cate.getImages();
                if (oldImage != null && !oldImage.startsWith("http")) {
                    File oldFile = new File(Constant.DIR + "/" + oldImage);
                    if (oldFile.exists() && oldFile.isFile()) {
                        oldFile.delete();
                    }
                }
            }
            if (categoryRepository != null) {
                categoryRepository.deleteById(id);
            } else {
                cateDao.delete(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        if (categoryRepository != null) {
            return (int) categoryRepository.count();
        }
        return cateDao.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll(int page, int pagesize) {
        return cateDao.findAll(page, pagesize);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findByCategoryname(String name) {
        try {
            if (categoryRepository != null) {
                List<Category> list = categoryRepository.searchByName(name);
                for (Category c : list) {
                    if (c.getCategoryname() != null && c.getCategoryname().equalsIgnoreCase(name)) {
                        return c;
                    }
                }
            }
            return cateDao.findByCategoryname(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
