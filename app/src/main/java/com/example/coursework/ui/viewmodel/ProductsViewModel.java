package com.example.coursework.ui.viewmodel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.ProductRepositoryImpl;
import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.ChosenIngredient;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductsViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    ProductRepository productRepository = new ProductRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();
    public MutableLiveData<List<BakeryProduct>> bakeryProducts = new MutableLiveData<>();
    public MutableLiveData<BakeryProduct> bakeryProduct = new MutableLiveData<>();
    public MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();
    public MutableLiveData<Ingredient> ingredient = new MutableLiveData<>();
    private final MutableLiveData<List<ChosenIngredient>> _chosenIngredients = new MutableLiveData<>();
    public LiveData<List<ChosenIngredient>> chosenIngredients = _chosenIngredients;
    private MutableLiveData<Pair<Integer, Ingredient>> removedIngredient = new MutableLiveData<>();

    private final MutableLiveData<Event<String>> _helpText = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = _helpText;

    private final MutableLiveData<Boolean> _showAdminFunctions = new MutableLiveData<>();
    public LiveData<Boolean> showAdminFunctions = _showAdminFunctions;

    public ProductsViewModel() {
        getIngredients();
        getBakeryProducts();
        getUserRole();
    }

    private void setHelpText(String text) {
        _helpText.postValue(null);
        _helpText.postValue(new Event<>(text));
    }


    public void getUserRole() {
        disposables.add(userRepository.getLoggedUserRole().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(role -> {
            _showAdminFunctions.postValue(role.getValue() != UserType.USER.getValue());
        }));
    }


    //----------------------------------------------------------------------------------------------

    public void getIngredients() {
        disposables.add(productRepository.getAllIngredients().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(item -> ingredients.postValue(item)));
    }

    public void createIngredient(String name, String measurement, double price) {
        if (name.isEmpty() || measurement.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(productRepository.insertIngredient(new Ingredient(0, name, measurement, price)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    public void getIngredient(int id) {
        disposables.add(productRepository.getIngredientById(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(ingredient::postValue));
    }

    public void removeIngredient(int position) {
        int id = ingredients.getValue().get(position).getId();
        removedIngredient.setValue(new Pair<>(position, ingredients.getValue().get(position)));
        disposables.add(productRepository.removeIngredient(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());

    }

    public void updateIngredient(int id, String name, String measurement, double price) {
        if (name.isEmpty() || measurement.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(productRepository.updateIngredient(new Ingredient(id, name, measurement, price)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());

    }

    public void cancelDeleting() {
        productRepository.insertIngredient(removedIngredient.getValue().second).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

    }
    //----------------------------------------------------------------------------------------------

    public void clearChosenIngredients() {
        _chosenIngredients.postValue(null);
    }

    public void updateChosenIngredients(List<ChosenIngredient> ingredients) {
        _chosenIngredients.postValue(ingredients);
    }

    public void getChosenIngredients(Integer productId) {
        disposables.add(Completable.fromAction(() -> {
            List<ChosenIngredient> newData = new ArrayList<>();
            if (ingredients.getValue() != null)
                for (Ingredient ingredient : ingredients.getValue()) {
                    newData.add(new ChosenIngredient(ingredient, 0.0));
                }
            if (productId == null) {
                if (chosenIngredients.getValue() != null) return;
                updateChosenIngredients(newData);
            } else {
                List<ChosenIngredient> ingredients = bakeryProduct.getValue().getIngredients();
                for (ChosenIngredient ingredient1 : ingredients) {
                    for (int i = 0; i < newData.size(); i++) {
                        if (newData.get(i).getId() == ingredient1.getId()) {
                            newData.get(i).setCount(ingredient1.getCount());
                        }
                    }
                }
                updateChosenIngredients(newData);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe());

    }

    //----------------------------------------------------------------------------------------------

    public void getBakeryProducts() {
        disposables.add(productRepository.getAllProducts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bakeryProducts::postValue));
    }

    public void createProduct(String name, String description, double price, String imageUri) {
        if (name.isEmpty() || description.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(Completable.fromAction(() -> {
            disposables.add(productRepository.insertProduct(new BakeryProduct(0, name, description, price, getShortListIngredients(), imageUri)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    private List<ChosenIngredient> getShortListIngredients() {
        List<ChosenIngredient> ingredients = chosenIngredients.getValue();
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        } else {
            ingredients = chosenIngredients.getValue().stream().map(ingredient -> {
                if (ingredient.getCount() != 0.0) {
                    return ingredient;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return ingredients;
    }

    public void updateProduct(int id, String name, String description, double price, String imageUrl) {
        if (name.isEmpty() || description.isEmpty()){
            setHelpText("Параметр не может быть пустым");
            return;
        }
        disposables.add(productRepository.updateProduct(new BakeryProduct(id, name, description, price, getShortListIngredients(), imageUrl)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    public void deleteProduct(int id) {
        disposables.add(productRepository.removeProduct(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    public void getProduct(int id) {
        disposables.add(productRepository.getProduct(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bakeryProduct::postValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
