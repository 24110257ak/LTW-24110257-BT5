package com.koha.service.impl;

import java.io.File;
import java.util.List;

import com.koha.dao.IProductDao;
import com.koha.dao.impl.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koha.entity.Product;
import com.koha.repository.ProductRepository;
import com.koha.service.IProductService;
import com.koha.util.Constant;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    private IProductDao productDao = new ProductDao();
    @Autowired
    private ProductRepository productRepository;

    public ProductServiceImpl() {
    }

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void insert(Product product) {
        productDao.insert(product);
        productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        Product oldProduct = productDao.findById(product.getProductId());
        Product oldProduct = findById(product.getProductId());
        if (oldProduct != null) {
            // Nếu có ảnh mới và ảnh cũ là file lưu trên ổ đĩa thì xóa ảnh cũ
            if (product.getImages() != null && !product.getImages().equals(oldProduct.getImages())) {
                String oldImage = oldProduct.getImages();
                if (oldImage != null && !oldImage.startsWith("http") && !oldImage.equals(Constant.DEFAULT_FILENAME)) {
                    File oldFile = new File(Constant.DIR + "/" + oldImage);
                    if (oldFile.exists() && oldFile.isFile()) {
                        oldFile.delete();
                    }
                }
            }
            productDao.update(product);
            productRepository.save(product);
        }
    }

    @Override
    public void delete(int productId) throws Exception {
        Product product = productDao.findById(productId);
        Product product = findById(productId);
        if (product != null) {
            String oldImage = product.getImages();
            if (oldImage != null && !oldImage.startsWith("http") && !oldImage.equals(Constant.DEFAULT_FILENAME)) {
                File oldFile = new File(Constant.DIR + "/" + oldImage);
                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.delete();
                }
            }
            productDao.delete(productId);
            productRepository.deleteById(productId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(int productId) {
        return productDao.findById(productId);
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String productName) {
        return productDao.searchByName(productName);
        return productRepository.searchByName(productName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findTop10() {
        return productDao.findTop10();
        return productRepository.findTop10ByOrderByProductIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll(int page, int pageSize) {
        return productDao.findAll(page, pageSize);
        return productRepository.findAllByOrderByProductIdDesc(PageRequest.of(page, pageSize)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        return productDao.count();
        return (int) productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(int categoryId) {
        return productDao.findByCategoryId(categoryId);
        return productRepository.findByCategory_CategoryId(categoryId);
    }
}

