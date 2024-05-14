package com.example.coursework.ui.viewmodel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.App;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.ChosenIngredient;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductsViewModel extends ViewModel {
    ProductRepository productRepository = new ProductRepository();
    public LiveData<List<BakeryProduct>> bakeryProducts;
    public LiveData<List<Ingredient>> ingredients = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ChosenIngredient>> chosenIngredients = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer, Ingredient>> removedIngredient = new MutableLiveData<>();

//    public LiveData<Boolean> getShowCheck() {
//        return showCheck;
//    }
//
//    public void updateCheckVisibility(boolean isVisible) {
//        showCheck.setValue(isVisible);
//    }


    public ProductsViewModel() {
        setIngredients();
        setBakeryProducts();
    }

    public void getChosenIngredients(Integer productId){
        if (productId == null){
            List<ChosenIngredient> newData = new ArrayList<>();
            if (ingredients.getValue() != null)
                for (Ingredient ingredient : ingredients.getValue()) {
                    newData.add(new ChosenIngredient(ingredient, 0.0));
                }
            ((MutableLiveData<List<ChosenIngredient>>) chosenIngredients).setValue(newData);
        } else {
            productRepository.getIngredientsByProductId(productId);
            chosenIngredients = productRepository.getChosenIngredients();
        }
    }


    public void setBakeryProducts() {
        bakeryProducts = productRepository.getAllProducts();
    }

    public void setIngredients() {
        ingredients = productRepository.getAllIngredients();
    }

    public void cancelDeleting() {
        productRepository.insert(Objects.requireNonNull(removedIngredient.getValue()).second);
    }

    public LiveData<Ingredient> setIngredient(int id) {
        return productRepository.getIngredientById(id);
    }



    public void createIngredient(String name, String measurement, double price){
        productRepository.insert(new Ingredient(0, name, measurement, price));
    }

    public void removeIngredient(int position) {
        int id = ingredients.getValue().get(position).getId();
        removedIngredient.setValue(new Pair<>(position, ingredients.getValue().get(position)));
        productRepository.removeIngredient(id);
    }

    public void updateIngredient(int id, String name, String measurement, double price) {
        productRepository.updateIngredient(
                new Ingredient(id, name, measurement, price)
        );
    }
}
