package com.example.coursework.data.database;

import androidx.room.TypeConverter;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.ui.entities.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<IngredientEntity> ingredientFromString(String value) {
        Type listType = new TypeToken<List<IngredientEntity>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String ingredientFromList(List<IngredientEntity> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static ProductEntity productFromString(String value) {
        Type listType = new TypeToken<ProductEntity>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String productFromList(ProductEntity list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
