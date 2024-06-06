package com.example.coursework.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "production")
public class ProductionEntity {
    @PrimaryKey
    @NonNull
    String id;
    ProductEntity product;
    String productId;
    Integer count;
    Long startTime;
    Long endTime;

    public ProductionEntity(@NonNull String id, ProductEntity product, Integer count, Long startTime, Long endTime) {
        this.id = id;
        this.product = product;
        this.productId = product.getId();
        this.count = count;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ProductionEntity() {

    }

    public Double getCostPrice() {
        double sum = 0.0;
        for (int i = 0; i < product.getIngredients().size(); i++) {
            sum += product.getIngredients().get(i).getPrice();
        }
        return sum;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
