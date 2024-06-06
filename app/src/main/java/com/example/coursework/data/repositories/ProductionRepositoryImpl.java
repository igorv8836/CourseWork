package com.example.coursework.data.repositories;


import android.annotation.SuppressLint;
import android.util.Log;

import com.example.coursework.App;
import com.example.coursework.data.database.ProductionDao;
import com.example.coursework.data.database.entities.ProductionEntity;
import com.example.coursework.data.firebase.firestore.ProductionFirestore;
import com.example.coursework.domain.repositories.ProductionRepository;
import com.example.coursework.ui.entities.BakeryProduction;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductionRepositoryImpl implements ProductionRepository {
    private final ProductionDao dao;
    private final ProductionFirestore firestore;

    public ProductionRepositoryImpl() {
        dao = App.getDb().getProductionDao();
        firestore = new ProductionFirestore();
    }

    @SuppressLint("CheckResult")
    public Observable<List<BakeryProduction>> getProductions() {
        firestore.getAllProductions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productionEntities -> {
                    dao.deleteAllProductions().andThen(dao.insertProductions(productionEntities))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                            }, throwable -> {});
                }, throwable -> {});

        return dao.getAllProductions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformProductions);
    }

    @Override
    public Observable<BakeryProduction> getProduction(String id) {
        return dao.getProduction(id).map(BakeryProduction::fromProductionEntity).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable addProduction(BakeryProduction production) {
        return firestore.insertProduction(production.toProductionEntity())
                .flatMapCompletable(id -> {
                    production.setId(id);
                    return dao.insertProduction(production.toProductionEntity()).subscribeOn(Schedulers.io());
                });
    }

    @Override
    public Completable updateProduction(BakeryProduction production) {
        return firestore.updateProduction(production.toProductionEntity())
                .andThen(dao.updateProduction(production.toProductionEntity())).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteProduction(String id) {
        return firestore.deleteProduction(id)
                .andThen(dao.deleteProduction(id)).subscribeOn(Schedulers.io());
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable<List<BakeryProduction>> getProductionsByDate(long startDate, long endDate) {
        return dao.getProductionsByDate(startDate, endDate).map(this::transformProductions).subscribeOn(Schedulers.io());
    }

    private List<BakeryProduction> transformProductions(List<ProductionEntity> productionEntities) {
        List<BakeryProduction> data = new ArrayList<>();
        for (ProductionEntity productionEntity : productionEntities) {
            data.add(BakeryProduction.fromProductionEntity(productionEntity));
        }
        return data;
    }
}
