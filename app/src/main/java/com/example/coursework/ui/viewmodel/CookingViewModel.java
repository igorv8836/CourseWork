package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.ProductRepositoryImpl;
import com.example.coursework.data.repositories.ProductionRepositoryImpl;
import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.domain.repositories.ProductionRepository;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.BakeryProduction;
import com.example.coursework.ui.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CookingViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    ProductionRepository repository = new ProductionRepositoryImpl();
    ProductRepository productRepository = new ProductRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();

    private final MutableLiveData<List<BakeryProduct>> _products = new MutableLiveData<>();
    public LiveData<List<BakeryProduct>> products = _products;
    private final MutableLiveData<List<BakeryProduction>> _bakeryProductions = new MutableLiveData<>();
    public LiveData<List<BakeryProduction>> bakeryProductions = _bakeryProductions;
    private final MutableLiveData<BakeryProduction> _bakeryProduction = new MutableLiveData<>();
    public LiveData<BakeryProduction> bakeryProduction = _bakeryProduction;
    private final MutableLiveData<Event<String>> _helpText = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = _helpText;

    private final MutableLiveData<Boolean> _showAdminFunctions = new MutableLiveData<>();
    public LiveData<Boolean> showAdminFunctions = _showAdminFunctions;

    public CookingViewModel() {
        getProducts();
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

    public void getProducts() {
        disposables.add(
                productRepository.getAllProducts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_products::postValue)
        );
    }

    public void getProductions() {
        disposables.add(
                repository.getProductions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_bakeryProductions::postValue)
        );
    }

    public void getProduction(int id) {
        disposables.add(
                repository.getProduction(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_bakeryProduction::postValue)
        );
    }

    public void addProduction(int productPosition, int count, long startTime, long endTime) {
        if (count == 0 || startTime > endTime){
            setHelpText("Количество равно нулю или время старта больше времени окончания");
            return;
        }
        if (productPosition == -1){
            setHelpText("Изделие не выбрано");
            return;
        }
        disposables.add(Completable.fromAction(() -> {
            BakeryProduct product = null;
            if (products.getValue() != null)
                product = products.getValue().get(productPosition);
            else
                return;
            BakeryProduction production = new BakeryProduction(
                    null,
                    product,
                    count,
                    startTime,
                    endTime
            );
            disposables.add(
                    repository.addProduction(production)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
            );
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe());
    }

    public void deleteProduction(int id) {
        disposables.add(
                repository.deleteProduction(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    private BakeryProduct findProductById(int id) {
        BakeryProduct product = null;
        if (_products.getValue() != null)
            for (BakeryProduct p : _products.getValue()) {
                if (p.getId() == id) {
                    product = p;
                    break;
                }
            }
        return product;
    }

    public void updateProduction(int id, int productId, int count, long startTime, long endTime) {
        if (count == 0 || startTime > endTime){
            setHelpText("Количество равно нулю или время старта больше времени окончания");
            return;
        }
        disposables.add(Completable.fromAction(() -> {
                    BakeryProduct product = findProductById(productId);
                    BakeryProduction production = new BakeryProduction(
                            id,
                            product,
                            count,
                            startTime,
                            endTime
                    );
                    disposables.add(
                            repository.updateProduction(production)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                    );
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}