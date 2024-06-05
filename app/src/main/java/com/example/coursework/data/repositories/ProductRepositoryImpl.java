package com.example.coursework.data.repositories;

import android.annotation.SuppressLint;

import com.example.coursework.App;
import com.example.coursework.data.database.ProductDao;
import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.data.firebase.firestore.ProductFirestore;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;
    private final ProductFirestore productFirestore;

    public ProductRepositoryImpl() {
        productDao = App.getDb().getProductDao();
        productFirestore = new ProductFirestore();
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<List<Ingredient>> getAllIngredients() {
        productFirestore.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(ingredientEntities -> {
                    productDao.deleteAllIngredients()
                            .andThen(productDao.insertIngredients(ingredientEntities))
                            .subscribe(() -> {
                            }, throwable -> {
                            });
                });
        return productDao.getAllIngredients().map(this::transformIngredients);
    }

    @Override
    public Observable<Ingredient> getIngredientById(int id) {
        return productDao.getIngredient(id).map(Ingredient::fromEntity);
    }

    @Override
    public Completable updateIngredient(Ingredient ingredient) {
        return productDao.updateIngredient(ingredient.toEntity()).andThen(
                productFirestore.updateIngredient(
                        String.valueOf(ingredient.getId()),
                        ingredient.toEntity())
        );
    }

    @Override
    public Completable insertIngredient(Ingredient ingredient) {
        return productDao.insertIngredient(ingredient.toEntity())
                .flatMapCompletable(indx -> {
                    ingredient.setId(indx.intValue());
                    return productFirestore.insertIngredient(ingredient.toEntity());
                });
    }

    @Override
    public Completable removeIngredient(int id) {
        return productDao.deleteIngredient(id)
                .andThen(productFirestore.deleteIngredient(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Ingredient> transformIngredients(List<IngredientEntity> ingredientEntities) {
        List<Ingredient> data = new ArrayList<>();
        for (IngredientEntity ingredientEntity : ingredientEntities) {
            data.add(Ingredient.fromEntity(ingredientEntity));
        }
        return data;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<List<BakeryProduct>> getAllProducts() {
        productFirestore.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(productEntities -> {
                    List<ProductEntity> entities = new ArrayList<>(productEntities);
                    productDao.deleteAllProducts().andThen(productDao.insertProducts(entities))
                            .subscribe(() -> {
                            }, throwable -> {
                            });
                });

        return productDao.getAllProducts().map(this::transformProducts);
    }

    @Override
    public Observable<BakeryProduct> getProduct(int id) {
        return productDao.getProduct(id).map(BakeryProduct::fromProductEntity);
    }

    @Override
    public Completable insertProduct(BakeryProduct product) {
        return productDao.insertProduct(product.toProductEntity())
                .andThen(productFirestore.insertProduct(product.toProductEntity()));
    }

    @Override
    public Completable updateProduct(BakeryProduct product) {
        return productDao.updateProduct(product.toProductEntity())
                .andThen(productFirestore.updateProduct(String.valueOf(product.getId()), product.toProductEntity()));
    }

    @Override
    public Completable removeProduct(int id) {
        return productDao.deleteProduct(id)
                .andThen(productFirestore.deleteProduct(String.valueOf(id)));
    }

    private List<BakeryProduct> transformProducts(List<ProductEntity> productEntities) {
        List<BakeryProduct> data = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            data.add(BakeryProduct.fromProductEntity(productEntity));
        }
        return data;
    }
}
