package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.data.database.entities.ProductWithIngredients;

import java.util.ArrayList;
import java.util.List;

public class BakeryProduct {
    private int id;
    private String name;
    private String description;
    private double price;
    private List<Ingredient> ingredients;
    private String imageUri;

    public BakeryProduct(int id, String name, String description, double price, List<Ingredient> ingredients, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductWithIngredients toProductWithIngredients() {
        List<IngredientEntity> ingredients = new ArrayList<>();
        for (Ingredient ingredient : getIngredients()) {
            ingredients.add(
                    new IngredientEntity(
                            ingredient.getId(),
                            ingredient.getName(),
                            ingredient.getMeasurementText(),
                            ingredient.getPrice()
                    )
            );
        }
        return new ProductWithIngredients(
                new ProductEntity(id, name, description, price, imageUri),
                ingredients
        );
    }

    public static BakeryProduct fromProductWithIngredients(ProductWithIngredients entity) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientEntity ingredient : entity.getIngredients()) {
            ingredients.add(Ingredient.fromEntity(ingredient));
        }
        return new BakeryProduct(
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getProduct().getDescription(),
                entity.getProduct().getPrice(),
                ingredients,
                entity.getProduct().getImageUri()
        );
    }
}
