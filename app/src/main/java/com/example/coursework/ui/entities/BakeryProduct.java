package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class BakeryProduct {
    private String id;
    private String name;
    private String description;
    private double price;
    private List<ChosenIngredient> ingredients;
    private String imageUri;

    public BakeryProduct(String id, String name, String description, double price, List<ChosenIngredient> ingredients, String imageUri) {
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

    public List<ChosenIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ChosenIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductEntity toProductEntity() {
        List<IngredientEntity> ingredients = new ArrayList<>();
        for (ChosenIngredient ingredient : getIngredients()) {
            ingredients.add(ingredient.toEntity());
        }
        return new ProductEntity(
                getId(),
                getName(),
                getDescription(),
                getPrice(),
                getImageUri(),
                ingredients);
    }

    public static BakeryProduct fromProductEntity(ProductEntity entity) {
        List<ChosenIngredient> ingredients = new ArrayList<>();
        for (IngredientEntity ingredient : entity.getIngredients()) {
            ingredients.add(ChosenIngredient.fromEntity(ingredient));
        }
        return new BakeryProduct(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                ingredients,
                entity.getImageUri()
        );
    }
}
