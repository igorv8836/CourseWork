package com.example.coursework.domain.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.coursework.App;
import com.example.coursework.data.database.AppDatabase;
import com.example.coursework.data.database.ProductDao;
import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.ChosenIngredient;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface ProductRepository {
    Observable<List<Ingredient>> getAllIngredients();
    Observable<Ingredient> getIngredientById(String id);
    Completable updateIngredient(Ingredient ingredient);
    Completable insertIngredient(Ingredient ingredient);
    Completable removeIngredient(String id);

    Observable<List<BakeryProduct>> getAllProducts();
    Observable<BakeryProduct> getProduct(String id);
    Completable insertProduct(BakeryProduct product);
    Completable updateProduct(BakeryProduct product);
    Completable removeProduct(String id);
}
