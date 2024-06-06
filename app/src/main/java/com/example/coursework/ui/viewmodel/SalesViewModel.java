package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.ProductRepositoryImpl;
import com.example.coursework.data.repositories.SalesRepositoryImpl;
import com.example.coursework.data.repositories.UserRepositoryImpl;
import com.example.coursework.domain.repositories.ProductRepository;
import com.example.coursework.domain.repositories.SalesRepository;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.addClasses.Event;
import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.ProductSale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SalesViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SalesRepository repository = new SalesRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final MutableLiveData<List<BakeryProduct>> _products = new MutableLiveData<>();
    public LiveData<List<BakeryProduct>> products = _products;
    private final MutableLiveData<List<ProductSale>> _sales = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ProductSale>> sales = _sales;

    private final MutableLiveData<Boolean> _showAdminFunctions = new MutableLiveData<>();
    public LiveData<Boolean> showAdminFunctions = _showAdminFunctions;

    private final MutableLiveData<Event<String>> _helpText = new MutableLiveData<>();
    public MutableLiveData<Event<String>> helpText = _helpText;

    public SalesViewModel() {
        getProducts();
        getUserRole();
    }

    private void setHelpText(String text) {
        _helpText.postValue(null);
        _helpText.postValue(new Event<>(text));
    }

    public void getUserRole() {
        disposables.add(userRepository.getLoggedUserType().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(role -> _showAdminFunctions.postValue(role.getValue() != UserType.USER.getValue()), t -> {}));
    }

    public void getProducts() {
        disposables.add(
                productRepository.getAllProducts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_products::postValue, t -> {})
        );
    }

    public void getSales(){
        disposables.add(
                repository.getSales()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(_sales::postValue, t -> {})
        );
    }

    public void addSale(int productPosition, int count, long time, double price) {
        if (count == 0 || productPosition == -1){
            setHelpText("Количество равно нулю или изделие не выбрано");
            return;
        }
        disposables.add(Completable.fromAction(() -> {
                    BakeryProduct product;
                    if (products.getValue() != null)
                        product = products.getValue().get(productPosition);
                    else
                        return;
                    ProductSale sale = new ProductSale(
                            "",
                            product,
                            price,
                            time,
                            count
                    );
                    disposables.add(
                            repository.addSale(sale)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {}, t -> {})
                    );
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, t -> {}));
    }

    public void deleteSale(String id){
        disposables.add(
                repository.deleteSale(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {}, t -> {})
        );
    }
}