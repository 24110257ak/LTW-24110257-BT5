package com.koha.service.impl;

import java.io.File;
import java.util.List;

import com.koha.dao.IProductDao;
import com.koha.dao.impl.ProductDao;
import com.koha.entity.Product;
import com.koha.service.IProductService;
import com.koha.util.Constant;

public class ProductServiceImpl implements IProductService {

    private IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        Product oldProduct = productDao.findById(product.getProductId());
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
        }
    }

    @Override
    public void delete(int productId) throws Exception {
        Product product = productDao.findById(productId);
        if (product != null) {
            String oldImage = product.getImages();
            if (oldImage != null && !oldImage.startsWith("http") && !oldImage.equals(Constant.DEFAULT_FILENAME)) {
                File oldFile = new File(Constant.DIR + "/" + oldImage);
                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.delete();
                }
            }
            productDao.delete(productId);
        }
    }

    @Override
    public Product findById(int productId) {
        return productDao.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> searchByName(String productName) {
        return productDao.searchByName(productName);
    }

    @Override
    public List<Product> findTop10() {
        return productDao.findTop10();
    }

    @Override
    public List<Product> findAll(int page, int pageSize) {
        return productDao.findAll(page, pageSize);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        return productDao.findByCategoryId(categoryId);
    }
}
