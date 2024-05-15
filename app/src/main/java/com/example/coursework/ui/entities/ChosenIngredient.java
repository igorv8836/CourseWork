package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.IngredientEntity;

public class ChosenIngredient extends Ingredient {
    Double count;

    public ChosenIngredient(int id, String name, String measurementText, Double price, Double count){
        super(id, name, measurementText, price);
        this.count = count;
    }

    public ChosenIngredient(Ingredient ingredient, double count) {
        super(ingredient.getId(), ingredient.getName(), ingredient.getMeasurementText(), ingredient.getPrice());
        this.count = count;
    }

    public ChosenIngredient(Ingredient ingredient) {
        super(ingredient.getId(), ingredient.getName(), ingredient.getMeasurementText(), ingredient.getPrice());
        this.count = 0.0;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public boolean isChosen(){
        return count != 0.0;
    }

    @Override
    public IngredientEntity toEntity(){
        return new IngredientEntity(getId(), getName(), getMeasurementText(), getPrice(), getCount());
    }

    public static ChosenIngredient fromEntity(IngredientEntity entity){
        return new ChosenIngredient(entity.getId(), entity.getName(), entity.getMeasurementText(), entity.getPrice(), entity.getCount());
    }
}
