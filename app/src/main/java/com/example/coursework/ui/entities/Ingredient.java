package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.IngredientEntity;

public class Ingredient {

    public Ingredient(int id, String name, String measurementText, Double price){
        this.id = id;
        this.name = name;
        this.measurementText = measurementText;
        this.price = price;
    }
    private int id;
    private String name;
    private String measurementText;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementText() {
        return measurementText;
    }

    public void setMeasurementText(String measurementText) {
        this.measurementText = measurementText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public IngredientEntity toEntity(){
        return new IngredientEntity(getId(), getName(), getMeasurementText(), getPrice());
    }

    public static Ingredient fromEntity(IngredientEntity entity){
        return new Ingredient(entity.getId(), entity.getName(), entity.getMeasurementText(), entity.getPrice());
    }
}
