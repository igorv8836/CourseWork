package com.example.coursework.domain.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.coursework.App;
import com.example.coursework.data.database.AppDatabase;
import com.example.coursework.data.database.ProductDao;
import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductWithIngredients;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.ChosenIngredient;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final ProductDao productDao;
    private LiveData<List<ProductWithIngredients>> allProducts;
    private LiveData<List<IngredientEntity>> allIngredients;
    private LiveData<ProductWithIngredients> chosenProduct;

    public ProductRepository() {
        productDao = App.getDb().getProductDao();
        allProducts = productDao.getProductWithIngredients();
        allIngredients = productDao.getAllIngredients();
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return Transformations.map(allIngredients, this::transformIngredients);
    }

    public LiveData<Ingredient> getIngredientById(int id) {
        LiveData<IngredientEntity> data = productDao.getIngredient(id);
        return Transformations.map(data, Ingredient::fromEntity);
    }

    public void updateIngredient(Ingredient ingredient) {
        IngredientEntity ingredientEntity = ingredient.toEntity();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.updateIngredient(ingredientEntity);
        });
    }

    public void insert(Ingredient ingredient) {
        IngredientEntity ingredientEntity = ingredient.toEntity();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insertIngredient(ingredientEntity);
        });
    }

    public void removeIngredient(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.deleteIngredient(id);
        });
    }

    public LiveData<List<BakeryProduct>> getAllProducts() {
        return Transformations.map(allProducts, this::transformProducts);
    }

    public void insert(BakeryProduct product) {
        ProductWithIngredients productWithIngredients = product.toProductWithIngredients();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insertProductWithIngredients(productWithIngredients.getProduct(), productWithIngredients.getIngredients());
        });
    }

    public void getIngredientsByProductId(Integer productId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chosenProduct = productDao.getProductWithIngredientsById(productId);
        });
    }

    public LiveData<List<ChosenIngredient>> getChosenIngredients() {
        return Transformations.map(chosenProduct, this::transformChosenIngredients);
    }

    private List<BakeryProduct> transformProducts(List<ProductWithIngredients> productEntities) {
        List<BakeryProduct> data = new ArrayList<>();
        for (ProductWithIngredients productEntity : productEntities) {
            data.add(BakeryProduct.fromProductWithIngredients(productEntity));
        }
        return data;
    }

    private List<Ingredient> transformIngredients(List<IngredientEntity> ingredientEntities) {
        List<Ingredient> data = new ArrayList<>();
        for (IngredientEntity ingredientEntity : ingredientEntities) {
            data.add(Ingredient.fromEntity(ingredientEntity));
        }
        return data;
    }

    private List<ChosenIngredient> transformChosenIngredients(ProductWithIngredients product) {
        List<ChosenIngredient> data = new ArrayList<>();
        for (IngredientEntity ingredient : product.getIngredients()) {
            data.add(new ChosenIngredient(Ingredient.fromEntity(ingredient), ingredient.getCount()));
        }
        return data;
    }
}
