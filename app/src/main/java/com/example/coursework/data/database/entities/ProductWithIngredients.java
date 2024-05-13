package com.example.coursework.data.database.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ProductWithIngredients {
    @Embedded
    public ProductEntity product;
    @Relation(
            parentColumn = "productId",
            entityColumn = "ingredientId",
            associateBy = @Junction(ProductIngredientCrossRef.class)
    )
    public List<IngredientEntity> ingredients;


    public ProductWithIngredients(ProductEntity product, List<IngredientEntity> ingredients) {
        this.product = product;
        this.ingredients = ingredients;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }
}
