package com.koha.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koha.dao.IProductDao;
import com.koha.dao.impl.ProductDao;
import com.koha.entity.Product;
import com.koha.repository.ProductRepository;
import com.koha.service.IProductService;
import com.koha.util.Constant;

@Service
@Primary
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired(required = false)
    private ProductRepository productRepository;

    private IProductDao productDao = new ProductDao();

    public ProductServiceImpl() {
    }

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void insert(Product product) {
        if (productRepository != null) {
            productRepository.save(product);
        } else {
            productDao.insert(product);
        }
    }

    @Override
    public void update(Product product) {
        Product oldProduct = findById(product.getProductId());
        if (oldProduct != null) {
            if (product.getImages() != null && !product.getImages().equals(oldProduct.getImages())) {
                String oldImage = oldProduct.getImages();
                if (oldImage != null && !oldImage.startsWith("http") && !oldImage.equals(Constant.DEFAULT_FILENAME)) {
                    File oldFile = new File(Constant.DIR + "/" + oldImage);
                    if (oldFile.exists() && oldFile.isFile()) {
                        oldFile.delete();
                    }
                }
            }
            if (productRepository != null) {
                productRepository.save(product);
            } else {
                productDao.update(product);
            }
        }
    }

    @Override
    public void delete(int productId) throws Exception {
        Product product = findById(productId);
        if (product != null) {
            String oldImage = product.getImages();
            if (oldImage != null && !oldImage.startsWith("http") && !oldImage.equals(Constant.DEFAULT_FILENAME)) {
                File oldFile = new File(Constant.DIR + "/" + oldImage);
                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.delete();
                }
            }
            if (productRepository != null) {
                productRepository.deleteById(productId);
            } else {
                productDao.delete(productId);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(int productId) {
        if (productRepository != null) {
            return productRepository.findById(productId).orElse(null);
        }
        return productDao.findById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        if (productRepository != null) {
            return productRepository.findAll();
        }
        return productDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String productName) {
        if (productRepository != null) {
            return productRepository.searchByName(productName);
        }
        return productDao.searchByName(productName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findTop10() {
        if (productRepository != null) {
            return productRepository.findTop10ByOrderByProductIdDesc();
        }
        return productDao.findTop10();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll(int page, int pageSize) {
        if (productRepository != null) {
            return productRepository.findAllByOrderByProductIdDesc(PageRequest.of(page, pageSize)).getContent();
        }
        return productDao.findAll(page, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        if (productRepository != null) {
            return (int) productRepository.count();
        }
        return productDao.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(int categoryId) {
        if (productRepository != null) {
            return productRepository.findByCategory_CategoryId(categoryId);
        }
        return productDao.findByCategoryId(categoryId);
    }
}
