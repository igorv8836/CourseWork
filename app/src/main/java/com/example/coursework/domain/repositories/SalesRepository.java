package com.example.coursework.domain.repositories;


import com.example.coursework.ui.entities.ProductSale;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface SalesRepository {
    Observable<List<ProductSale>> getSales();

    Observable<ProductSale> getSale(String id);

    Completable addSale(ProductSale sale);

    Completable updateSale(ProductSale sale);

    Completable deleteSale(String id);

    Observable<List<ProductSale>> getSalesByDate(long startDate, long endDate);

    Observable<Double> getRevenue(long startDate, long endDate);
}
