package com.example.coursework.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.data.database.entities.ProductIngredientCrossRef;
import com.example.coursework.data.database.entities.ProductWithIngredients;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    long insertProduct(ProductEntity product);

    @Update
    void updateProduct(ProductEntity product);

    @Delete
    void deleteProduct(ProductEntity product);

    @Insert
    long insertIngredient(IngredientEntity ingredient);

    @Update
    void updateIngredient(IngredientEntity ingredient);

    @Query("DELETE FROM ingredients WHERE ingredientId = :id")
    void deleteIngredient(int id);

    @Query("SELECT * FROM ingredients WHERE ingredientId = :id")
    LiveData<IngredientEntity> getIngredient(int id);

    @Query("SELECT * FROM ingredients")
    LiveData<List<IngredientEntity>> getAllIngredients();

    @Insert
    void insertProductIngredientCrossRef(ProductIngredientCrossRef ref);

    @Delete
    void deleteProductIngredientCrossRef(ProductIngredientCrossRef ref);

    @Transaction
    default void insertProductWithIngredients(ProductEntity product, List<IngredientEntity> ingredients) {
        Long id = insertProduct(product);
        for (IngredientEntity ingredient : ingredients) {
            ingredient.setId(Integer.parseInt(id.toString()));
            insertIngredient(ingredient);
        }
    }

    @Transaction
    @Query("SELECT * FROM products WHERE productId = :productId")
    LiveData<ProductWithIngredients> getProductWithIngredientsById(int productId);

    @Transaction
    @Query("SELECT * FROM products")
    LiveData<List<ProductWithIngredients>> getProductWithIngredients();

    @Transaction
    default void updateProductWithIngredients(ProductEntity product, List<IngredientEntity> ingredients) {
        updateProduct(product);
        for (IngredientEntity ingredient : ingredients) {
            updateIngredient(ingredient);
        }
    }

    @Transaction
    default void deleteProductWithIngredients(ProductEntity product, List<IngredientEntity> ingredients) {
        deleteProduct(product);
    }
}
