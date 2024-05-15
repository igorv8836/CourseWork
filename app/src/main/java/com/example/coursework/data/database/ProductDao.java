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

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface ProductDao {
    @Insert
    Completable insertIngredient(IngredientEntity ingredient);

    @Update
    Completable updateIngredient(IngredientEntity ingredient);

    @Query("DELETE FROM ingredients WHERE ingredientId = :id")
    Completable deleteIngredient(int id);

    @Query("SELECT * FROM ingredients WHERE ingredientId = :id")
    Observable<IngredientEntity> getIngredient(int id);

    @Query("SELECT * FROM ingredients")
    Observable<List<IngredientEntity>> getAllIngredients();

    //----------------------------------------------------------------------------------------------

    @Insert
    Completable insertProduct(ProductEntity product);

    @Update
    Completable updateProduct(ProductEntity product);

    @Query("DELETE FROM products WHERE productId = :id")
    Completable deleteProduct(int id);

    @Query("SELECT * FROM products WHERE productId = :id")
    Observable<ProductEntity> getProduct(int id);

    @Query("SELECT * FROM products")
    Observable<List<ProductEntity>> getAllProducts();
}
