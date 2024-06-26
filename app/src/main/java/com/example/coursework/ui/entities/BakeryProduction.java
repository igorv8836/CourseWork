package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.ProductionEntity;

public class BakeryProduction {
    String id;
    BakeryProduct product;
    Integer count;
    Long startTime;
    Long endTime;

    public BakeryProduction(String id, BakeryProduct product, Integer count, Long startTime, Long endTime) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Double getCostPrice() {
        double sum = 0.0;
        for (int i = 0; i < product.getIngredients().size(); i++) {
            sum += product.getIngredients().get(i).getPrice();
        }
        return sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BakeryProduct getProduct() {
        return product;
    }

    public void setProduct(BakeryProduct product) {
        this.product = product;
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

    public static BakeryProduction fromProductionEntity(ProductionEntity productionEntity) {
        return new BakeryProduction(
                productionEntity.getId(),
                BakeryProduct.fromProductEntity(productionEntity.getProduct()),
                productionEntity.getCount(),
                productionEntity.getStartTime(),
                productionEntity.getEndTime()
        );
    }

    public ProductionEntity toProductionEntity() {
        return new ProductionEntity(
                id,
                product.toProductEntity(),
                count,
                startTime,
                endTime
        );
    }
}
