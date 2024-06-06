package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.exchange_rate.ApiClient;
import com.example.coursework.data.exchange_rate.CbrApi;
import com.example.coursework.data.exchange_rate.CurrencyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExchangeRateViewModel extends ViewModel {
    private final CbrApi api;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<CurrencyResponse> _response = new MutableLiveData<>();
    private final MutableLiveData<List<String>> _rates = new MutableLiveData<>();
    public final LiveData<List<String>> rates = _rates;
    private final MutableLiveData<CurrencyResponse.Currency> _rate = new MutableLiveData<>();
    public final LiveData<CurrencyResponse.Currency> rate = _rate;


    public ExchangeRateViewModel() {
        api = ApiClient.getCbrApi();
        getRates();
    }

    public void getRates() {
        disposables.add(api.getDailyRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            _response.setValue(res);
                            List<String> values = new ArrayList<>();
                            for (CurrencyResponse.Currency currency : res.getValute().values()) {
                                values.add(currency.getName());
                            }
                            _rates.setValue(values);
                        },
                        t -> {}
                ));
    }

    public void getRate(int position) {
        CurrencyResponse.Currency currency = new ArrayList<>(Objects.requireNonNull(_response.getValue()).getValute().values()).get(position);
        _rate.setValue(currency);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
