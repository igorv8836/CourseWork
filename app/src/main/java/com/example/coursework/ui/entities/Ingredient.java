package com.example.coursework.ui.entities;

public class Ingredient {

    public Ingredient(int id, String name, String measurementText, Double count){
        this.id = id;
        this.name = name;
        this.measurementText = measurementText;
        this.count = count;
    }
    private int id;
    private String name;
    private String measurementText;
    private Double count;

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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
