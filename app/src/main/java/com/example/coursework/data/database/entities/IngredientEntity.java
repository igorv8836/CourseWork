package com.example.coursework.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class IngredientEntity {

    @ColumnInfo(name = "ingredientId")
    @PrimaryKey
    private @NonNull String id;
    private String name;
    private String measurementText;
    private Double price;
    private Double count;

    @Ignore
    public IngredientEntity(@NonNull String id, String name, String measurementText, Double price) {
        this.id = id;
        this.name = name;
        this.measurementText = measurementText;
        this.price = price;
        this.count = 0.0;
    }

    public IngredientEntity() {

    }


    public IngredientEntity(@NonNull String id, String name, String measurementText, Double price, Double count) {
        this.id = id;
        this.name = name;
        this.measurementText = measurementText;
        this.price = price;
        this.count = count;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
