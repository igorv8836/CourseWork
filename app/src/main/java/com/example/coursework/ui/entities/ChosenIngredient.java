package com.example.coursework.ui.entities;

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
}
