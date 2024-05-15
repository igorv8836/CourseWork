package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.domain.repositories.ProductionRepository;
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
    ProductionRepository repository = new ProductionRepository();
    ProductRepository productRepository = new ProductRepository();

    private final MutableLiveData<List<BakeryProduct>> _products = new MutableLiveData<>();
    public LiveData<List<BakeryProduct>> products = _products;
    private final MutableLiveData<List<BakeryProduction>> _bakeryProductions = new MutableLiveData<>();
    public LiveData<List<BakeryProduction>> bakeryProductions = _bakeryProductions;
    private final MutableLiveData<BakeryProduction> _bakeryProduction = new MutableLiveData<>();
    public LiveData<BakeryProduction> bakeryProduction = _bakeryProduction;

    public CookingViewModel() {
        getProducts();
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