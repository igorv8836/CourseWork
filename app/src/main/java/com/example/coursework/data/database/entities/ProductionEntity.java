package com.example.coursework.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coursework.ui.entities.BakeryProduct;

@Entity(tableName = "production")
public class ProductionEntity {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    ProductEntity product;
    Integer productId;
    Integer count;
    Long startTime;
    Long endTime;

    public ProductionEntity(Integer id, ProductEntity product, Integer count, Long startTime, Long endTime) {
        this.id = id;
        this.product = product;
        this.productId = product.getId();
        this.count = count;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Double getCostPrice(){
        double sum = 0.0;
        for (int i = 0; i < product.getIngredients().size(); i++){
            sum += product.getIngredients().get(i).getPrice();
        }
        return sum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
