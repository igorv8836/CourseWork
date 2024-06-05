package com.example.coursework.domain.repositories;


import com.example.coursework.App;
import com.example.coursework.data.database.SaleDao;
import com.example.coursework.data.database.entities.ProductionEntity;
import com.example.coursework.data.database.entities.SaleEntity;
import com.example.coursework.ui.entities.BakeryProduction;
import com.example.coursework.ui.entities.ProductSale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface SalesRepository {
    Observable<List<ProductSale>> getSales();
    Observable<ProductSale> getSale(int id);
    Completable addSale(ProductSale sale);
    Completable updateSale(ProductSale sale);
    Completable deleteSale(int id);
    Observable<List<ProductSale>> getSalesByDate(long startDate, long endDate);
    Observable<Double> getRevenue(long startDate, long endDate);
}
