package com.example.coursework.data.repositories;

import com.example.coursework.App;
import com.example.coursework.data.database.ProductDao;
import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryImpl() {
        productDao = App.getDb().getProductDao();
    }

    @Override
    public Observable<List<Ingredient>> getAllIngredients() {
        return productDao.getAllIngredients().map(this::transformIngredients);
    }

    @Override
    public Observable<Ingredient> getIngredientById(int id) {
        return productDao.getIngredient(id).map(Ingredient::fromEntity);
    }

    @Override
    public Completable updateIngredient(Ingredient ingredient) {
        return productDao.updateIngredient(ingredient.toEntity());
    }

    @Override
    public Completable insertIngredient(Ingredient ingredient) {
        return productDao.insertIngredient(ingredient.toEntity());
    }

    @Override
    public Completable removeIngredient(int id) {
        return productDao.deleteIngredient(id);
    }

    private List<Ingredient> transformIngredients(List<IngredientEntity> ingredientEntities) {
        List<Ingredient> data = new ArrayList<>();
        for (IngredientEntity ingredientEntity : ingredientEntities) {
            data.add(Ingredient.fromEntity(ingredientEntity));
        }
        return data;
    }

    @Override
    public Observable<List<BakeryProduct>> getAllProducts() {
        return productDao.getAllProducts().map(this::transformProducts);
    }

    @Override
    public Observable<BakeryProduct> getProduct(int id) {
        return productDao.getProduct(id).map(BakeryProduct::fromProductEntity);
    }

    @Override
    public Completable insertProduct(BakeryProduct product) {
        return productDao.insertProduct(product.toProductEntity());
    }

    @Override
    public Completable updateProduct(BakeryProduct product) {
        return productDao.updateProduct(product.toProductEntity());
    }

    @Override
    public Completable removeProduct(int id) {
        return productDao.deleteProduct(id);
    }

    private List<BakeryProduct> transformProducts(List<ProductEntity> productEntities) {
        List<BakeryProduct> data = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            data.add(BakeryProduct.fromProductEntity(productEntity));
        }
        return data;
    }
}