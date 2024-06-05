package com.example.coursework.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales")
public class SaleEntity {
    @PrimaryKey
    private @NonNull String id;
    private ProductEntity product;
    private String productId;
    private double salePrice;
    private long saleDate;
    private int count;

    public SaleEntity(@NonNull String id, ProductEntity product, double salePrice, long saleDate, int count) {
        this.id = id;
        this.product = product;
        this.productId = product.getId();
        this.salePrice = salePrice;
        this.saleDate = saleDate;
        this.count = count;
    }

    public SaleEntity(){

    }

    public double getRevenue() {
        return salePrice * count;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
