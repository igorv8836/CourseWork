package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.data.repositories.ProductionRepositoryImpl;
import com.example.coursework.data.repositories.SalesRepositoryImpl;
import com.example.coursework.domain.repositories.ProductionRepository;
import com.example.coursework.domain.repositories.SalesRepository;
import com.example.coursework.ui.entities.BakeryProduction;
import com.example.coursework.ui.entities.ProductSale;
import com.example.coursework.ui.entities.ReportElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SalesRepository salesRepository = new SalesRepositoryImpl();
    private final ProductionRepository productionRepository = new ProductionRepositoryImpl();
    private final MutableLiveData<List<ReportElement>> _sales = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ReportElement>> sales = _sales;
    private final MutableLiveData<List<ReportElement>> _productions = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ReportElement>> productions = _productions;

    private final MutableLiveData<Double> _revenue = new MutableLiveData<>(0.0);
    public LiveData<Double> revenue = _revenue;

    private final MutableLiveData<Double> _profit = new MutableLiveData<>(0.0);
    public LiveData<Double> profit = _profit;
    private final MutableLiveData<Double> _expenses = new MutableLiveData<>(0.0);
    public LiveData<Double> expenses = _expenses;
    // начальное время для периодов [сегодня, неделя, месяц, год, все время]
    private final MutableLiveData<List<Long>> periodTimes = new MutableLiveData<>();


    public ReportViewModel() {
        updateDates();

    }

    public void getReport(int period) {
        getProductions(period);
        getSales(period);
    }

    public void updateDates() {
        disposables.add(
                Completable.fromAction(() -> {
                            List<Long> times = new ArrayList<>();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date(System.currentTimeMillis()));

                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            times.add(calendar.getTimeInMillis());

                            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                            times.add(calendar.getTimeInMillis());

                            calendar.set(Calendar.DAY_OF_MONTH, 1);
                            times.add(calendar.getTimeInMillis());

                            calendar.set(Calendar.DAY_OF_YEAR, 1);
                            times.add(calendar.getTimeInMillis());

                            calendar.setTime(new Date(0));
                            times.add(calendar.getTimeInMillis());

                            periodTimes.postValue(times);
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
    }

    private void getProductions(int period) {
        disposables.add(productionRepository.getProductionsByDate(periodTimes.getValue().get(period), System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productions -> {
                    HashMap<String, ReportElement> data = new HashMap<>();
                    for (BakeryProduction production : productions) {
                        if (data.containsKey(production.getProduct().getId())) {
                            ReportElement element = data.get(production.getProduct().getId());
                            if (element != null) {
                                element.setCount(element.getCount() + production.getCount());
                            }
                        } else {
                            data.put(
                                    production.getProduct().getId(),
                                    new ReportElement(
                                            production.getProduct().getName(),
                                            production.getCount()
                                    )
                            );
                        }
                    }
                    _productions.postValue(new ArrayList<>(data.values()));
                }));
    }

    private void getSales(int period) {
        disposables.add(salesRepository.getSalesByDate(periodTimes.getValue().get(period), System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sales -> {
                    HashMap<String, ReportElement> data = new HashMap<>();
                    for (ProductSale sale : sales) {
                        if (data.containsKey(sale.getProduct().getId())) {
                            ReportElement element = data.get(sale.getProduct().getId());
                            if (element != null) {
                                element.setCount(element.getCount() + sale.getCount());
                                element.setRevenue(element.getRevenue() + sale.getRevenue());
                                element.setProfit(element.getProfit() + sale.getProfit());
                            }
                        } else {
                            data.put(
                                    sale.getProduct().getId(),
                                    new ReportElement(
                                            sale.getProduct().getName(),
                                            sale.getRevenue(),
                                            sale.getProfit(),
                                            sale.getCount(),
                                            false
                                    )
                            );
                        }
                    }
                    setStatisticsData(new ArrayList<>(data.values()));
                    _sales.postValue(new ArrayList<>(data.values()));
                }));
    }

    private void setStatisticsData(List<ReportElement> data){
        double profit = 0;
        double revenue = 0;
        for (ReportElement element : data) {
            profit += element.getProfit();
            revenue += element.getRevenue();
        }
        _profit.postValue(profit);
        _revenue.postValue(revenue);
        _expenses.postValue(revenue - profit);
    }
}