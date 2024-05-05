package com.example.coursework.ui.entities;

public class Ingredient {

    public Ingredient(int id, String name, Double price, Double weight){
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }
    private int id;
    private String name;
    private Double price;
    private Double weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
