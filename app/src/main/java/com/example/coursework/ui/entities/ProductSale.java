package com.example.coursework.ui.entities;

public class ProductSale {
    private int id;
    private BakeryProduct product;
    private double salePrice;
    private long saleDate;
    private int count;

    public ProductSale(int id, BakeryProduct product, double salePrice, long saleDate, int count) {
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
        return salePrice * count - getIngredientsPrice();
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
}