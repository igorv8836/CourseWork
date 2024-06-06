package com.example.coursework.data.repositories;


import android.annotation.SuppressLint;

import com.example.coursework.App;
import com.example.coursework.data.database.SaleDao;
import com.example.coursework.data.database.entities.SaleEntity;
import com.example.coursework.data.firebase.firestore.SaleFirestore;
import com.example.coursework.domain.repositories.SalesRepository;
import com.example.coursework.ui.entities.ProductSale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SalesRepositoryImpl implements SalesRepository {
    private final SaleDao dao;
    private final SaleFirestore firestore;

    public SalesRepositoryImpl() {
        dao = App.getDb().getSaleDao();
        firestore = new SaleFirestore();
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<List<ProductSale>> getSales() {
        firestore.getSales().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sales -> dao.deleteSales().andThen(dao.insertSales(sales))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, t -> {
                        }), throwable -> {
                });

        return dao.getSales()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformSales);
    }

    @Override
    public Observable<ProductSale> getSale(String id) {
        return dao.getSale(id).map(ProductSale::fromSaleEntity).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable addSale(ProductSale sale) {
        return firestore.insertSale(sale.toSaleEntity())
                .flatMapCompletable(id -> {
                    sale.setId(id);
                    return dao.insertSale(sale.toSaleEntity()).subscribeOn(Schedulers.io());
                });
    }

    @Override
    public Completable updateSale(ProductSale sale) {
        return firestore.updateSale(sale.toSaleEntity())
                .andThen(dao.updateSale(sale.toSaleEntity())).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteSale(String id) {
        return firestore.deleteSale(id)
                .andThen(dao.deleteSale(id)).subscribeOn(Schedulers.io());
    }

    private List<ProductSale> transformSales(List<SaleEntity> saleEntities) {
        List<ProductSale> data = new ArrayList<>();
        for (SaleEntity saleEntity : saleEntities) {
            data.add(ProductSale.fromSaleEntity(saleEntity));
        }
        return data;
    }

    @Override
    public Observable<List<ProductSale>> getSalesByDate(long startDate, long endDate) {
        return dao.getSalesByDate(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformSales);
    }

    @Override
    public Observable<Double> getRevenue(long startDate, long endDate) {
        return dao.getRevenue(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}