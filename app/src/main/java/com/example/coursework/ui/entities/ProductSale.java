package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.SaleEntity;

public class ProductSale {
    private String id;
    private BakeryProduct product;
    private double salePrice;
    private long saleDate;
    private int count;

    public ProductSale(String id, BakeryProduct product, double salePrice, long saleDate, int count) {
        this.id = id;
        this.product = product;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
        this.count = count;
    }


    public double getIngredientsPrice() {
        double ingredientsPrice = 0;
        for (Ingredient ingredient : product.getIngredients()) {
            ingredientsPrice += ingredient.getPrice();
        }
        return ingredientsPrice;
    }

    public double getProfit() {
        return (salePrice - getIngredientsPrice()) * count;
    }

    public double getRevenue() {
        return salePrice * count;
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

    public static ProductSale fromSaleEntity(SaleEntity saleEntity) {
        return new ProductSale(
                saleEntity.getId(),
                BakeryProduct.fromProductEntity(saleEntity.getProduct()),
                saleEntity.getSalePrice(),
                saleEntity.getSaleDate(),
                saleEntity.getCount());
    }

    public SaleEntity toSaleEntity() {
        return new SaleEntity(
                id,
                product.toProductEntity(),
                salePrice,
                saleDate,
                count);
    }
}
