package com.example.coursework.data.database;

import androidx.room.TypeConverter;

import com.example.coursework.ui.entities.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Ingredient> fromString(String value) {
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Ingredient> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
