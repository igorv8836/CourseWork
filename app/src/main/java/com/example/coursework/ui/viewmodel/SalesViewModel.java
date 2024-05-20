package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.domain.repositories.SalesRepository;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.BakeryProduction;
import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.entities.ProductSale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SalesViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SalesRepository repository = new SalesRepository();
    UserRepository userRepository = new UserRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final MutableLiveData<List<BakeryProduct>> _products = new MutableLiveData<>();
    public LiveData<List<BakeryProduct>> products = _products;
    private final MutableLiveData<List<ProductSale>> _sales = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ProductSale>> sales = _sales;

    private final MutableLiveData<Boolean> _showAdminFunctions = new MutableLiveData<>();
    public LiveData<Boolean> showAdminFunctions = _showAdminFunctions;

    public SalesViewModel() {
        getProducts();
        getUserRole();
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

    public void getSales(){
        disposables.add(
                repository.getSales()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_sales::postValue)
        );
    }

    public void addProduction(int productPosition, int count, long time, double price) {
        disposables.add(Completable.fromAction(() -> {
                    BakeryProduct product = null;
                    if (products.getValue() != null)
                        product = products.getValue().get(productPosition);
                    else
                        return;
                    ProductSale sale = new ProductSale(
                            0,
                            product,
                            price,
                            time,
                            count
                    );
                    disposables.add(
                            repository.addSale(sale)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe()
                    );
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    public void deleteSale(int id){
        disposables.add(
                repository.deleteSale(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }
}