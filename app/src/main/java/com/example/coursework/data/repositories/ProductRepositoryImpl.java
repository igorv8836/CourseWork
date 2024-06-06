package com.example.coursework.data.repositories;

import android.annotation.SuppressLint;
import android.net.Uri;

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
    public Observable<Ingredient> getIngredientById(String id) {
        return productDao.getIngredient(id).map(Ingredient::fromEntity);
    }

    @Override
    public Completable updateIngredient(Ingredient ingredient) {
        return productDao.updateIngredient(ingredient.toEntity()).andThen(
                productFirestore.updateIngredient(
                        ingredient.toEntity())
        );
    }

    @Override
    public Completable insertIngredient(Ingredient ingredient) {
        return productFirestore.insertIngredient(ingredient.toEntity())
                .flatMapCompletable(id -> {
                    ingredient.setId(id);
                    return productDao.insertIngredient(ingredient.toEntity());
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeIngredient(String id) {
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productEntities -> {
                    List<ProductEntity> entities = new ArrayList<>(productEntities);
                    productDao.deleteAllProducts().andThen(productDao.insertProducts(entities))
                            .subscribe(() -> {
                            }, throwable -> {});
                }, throwable -> {});

        return productDao.getAllProducts().map(this::transformProducts);
    }

    @Override
    public Observable<BakeryProduct> getProduct(String id) {
        return productDao.getProduct(id).map(BakeryProduct::fromProductEntity);
    }

    @Override
    public Completable insertProduct(BakeryProduct product, Uri uri) {
        return productFirestore.insertProduct(product.toProductEntity(), uri)
                .flatMapCompletable(id -> {
                    product.setId(id.first);
                    if (id.second != null)
                        product.setImageUri(id.second);
                    return productDao.insertProduct(product.toProductEntity()).subscribeOn(Schedulers.io());
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateProduct(BakeryProduct product) {
        return productDao.updateProduct(product.toProductEntity())
                .andThen(productFirestore.updateProduct(product.toProductEntity()));
    }

    @Override
    public Completable removeProduct(String id) {
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
