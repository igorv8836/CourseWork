package com.example.coursework.data.database.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "productIngredientCrossRef",
        primaryKeys = {"productId", "ingredientId"},
        indices = {@Index(value = "ingredientId")}
)
public class ProductIngredientCrossRef {
    public int productId;
    public int ingredientId;

    public ProductIngredientCrossRef(int productId, int ingredientId) {
        this.productId = productId;
        this.ingredientId = ingredientId;
    }
}
