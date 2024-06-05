package com.example.coursework.data.repositories;


import com.example.coursework.App;
import com.example.coursework.data.database.ProductionDao;
import com.example.coursework.data.database.entities.ProductionEntity;
import com.example.coursework.domain.repositories.ProductionRepository;
import com.example.coursework.ui.entities.BakeryProduction;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class ProductionRepositoryImpl implements ProductionRepository {
    private final ProductionDao dao;

    public ProductionRepositoryImpl() {
        dao = App.getDb().getProductionDao();
    }

    @Override
    public Observable<List<BakeryProduction>> getProductions() {
        return dao.getAllProductions().map(this::transformProductions);
    }

    @Override
    public Observable<BakeryProduction> getProduction(int id) {
        return dao.getProduction(id).map(BakeryProduction::fromProductionEntity);
    }

    @Override
    public Completable addProduction(BakeryProduction production) {
        return dao.insertProduction(production.toProductionEntity());
    }

    @Override
    public Completable updateProduction(BakeryProduction production) {
        return dao.updateProduction(production.toProductionEntity());
    }

    @Override
    public Completable deleteProduction(int id) {
        return dao.deleteProduction(id);
    }

    @Override
    public Observable<List<BakeryProduction>> getProductionsByDate(long startDate, long endDate) {
        return dao.getProductionsByDate(startDate, endDate).map(this::transformProductions);
    }

    private List<BakeryProduction> transformProductions(List<ProductionEntity> productionEntities) {
        List<BakeryProduction> data = new ArrayList<>();
        for (ProductionEntity productionEntity : productionEntities) {
            data.add(BakeryProduction.fromProductionEntity(productionEntity));
        }
        return data;
    }
}
