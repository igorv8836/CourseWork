package com.example.coursework.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.Ingredient;

import java.util.List;

@Entity(tableName = "products")
public class ProductEntity {

    @ColumnInfo(name = "productId")
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUri;
    private List<IngredientEntity> ingredients;

    public ProductEntity(int id, String name, String description, double price, String imageUri, List<IngredientEntity> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
        this.ingredients = ingredients;
    }

    public ProductEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }
}
