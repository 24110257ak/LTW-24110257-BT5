package com.koha.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.productId DESC")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private int productId;

    @Column(name = "ProductName", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String productName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX) NULL")
    private String description;

    @Column(name = "Price", columnDefinition = "DECIMAL(18,2) NOT NULL DEFAULT 0")
    private double price;

    @Column(name = "Images", columnDefinition = "NVARCHAR(500) NULL")
    private String images;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Status")
    private int status = 1; // 1: Đang bán, 0: Ngừng bán

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoryId", nullable = false)
    private Category category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Date createdDate = new Date();

    public Product() {
    }

    public Product(int productId, String productName, String description, double price, String images, int quantity,
            int status, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.images = images;
        this.quantity = quantity;
        this.status = status;
        this.category = category;
        this.createdDate = new Date();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    // Helper method formatted price for display
    public String getFormattedPrice() {
        return String.format("%,.0f đ", price);
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", productName=" + productName + ", price=" + price + ", quantity="
                + quantity + ", status=" + status + "]";
    }
}
