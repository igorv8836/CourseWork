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
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ProductDao {
    @Insert
    Completable insertIngredient(IngredientEntity ingredient);

    @Insert
    Completable insertIngredients(List<IngredientEntity> ingredients);

    @Update
    Completable updateIngredient(IngredientEntity ingredient);

    @Query("DELETE FROM ingredients WHERE ingredientId = :id")
    Completable deleteIngredient(String id);

    @Query("DELETE FROM ingredients")
    Completable deleteAllIngredients();

    @Query("SELECT * FROM ingredients WHERE ingredientId = :id")
    Observable<IngredientEntity> getIngredient(String id);

    @Query("SELECT * FROM ingredients")
    Observable<List<IngredientEntity>> getAllIngredients();

    //----------------------------------------------------------------------------------------------

    @Insert
    Completable insertProduct(ProductEntity product);

    @Insert
    Completable insertProducts(List<ProductEntity> products);

    @Update
    Completable updateProduct(ProductEntity product);

    @Query("DELETE FROM products WHERE productId = :id")
    Completable deleteProduct(String id);

    @Query("DELETE FROM products")
    Completable deleteAllProducts();

    @Query("SELECT * FROM products WHERE productId = :id")
    Observable<ProductEntity> getProduct(String id);

    @Query("SELECT * FROM products")
    Observable<List<ProductEntity>> getAllProducts();
}
