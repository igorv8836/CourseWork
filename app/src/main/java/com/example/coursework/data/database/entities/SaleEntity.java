package com.example.coursework.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales")
public class SaleEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private ProductEntity product;
    private int productId;
    private double salePrice;
    private long saleDate;
    private int count;

    public SaleEntity(int id, ProductEntity product, double salePrice, long saleDate, int count) {
        this.id = id;
        this.product = product;
        this.productId = product.getId();
        this.salePrice = salePrice;
        this.saleDate = saleDate;
        this.count = count;
    }

    public double getRevenue() {
        return salePrice * count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public long getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(long saleDate) {
        this.saleDate = saleDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
