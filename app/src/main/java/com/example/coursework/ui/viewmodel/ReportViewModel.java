package com.example.coursework.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework.ui.entities.BakeryProduct;
import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.entities.ProductSale;
import com.example.coursework.ui.entities.ReportElement;

import java.util.ArrayList;
import java.util.List;

public class ReportViewModel extends ViewModel {
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


    public ReportViewModel() {
        List<ReportElement> sales = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            sales.add(new ReportElement("Хлеб", 100, 50, 10, false));
        }
        _sales.setValue(sales);

        List<ReportElement> productions = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            productions.add(new ReportElement("Хлеб", 100, 50, 10, true));
        }
        _productions.setValue(productions);

        _revenue.setValue(1000.0);
        _profit.setValue(500.0);
        _expenses.setValue(500.0);
    }

    public void updateData(int position){

    }
}