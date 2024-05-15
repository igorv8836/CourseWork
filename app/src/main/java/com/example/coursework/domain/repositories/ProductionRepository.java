package com.example.coursework.domain.repositories;


import com.example.coursework.App;
import com.example.coursework.data.database.ProductionDao;
import com.example.coursework.data.database.entities.ProductionEntity;
import com.example.coursework.ui.entities.BakeryProduction;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class ProductionRepository {
    private final ProductionDao dao;

    public ProductionRepository() {
        dao = App.getDb().getProductionDao();
    }

    public Observable<List<BakeryProduction>> getProductions() {
        return dao.getAllProductions().map(this::transformProductions);
    }

    public Observable<BakeryProduction> getProduction(int id) {
        return dao.getProduction(id).map(BakeryProduction::fromProductionEntity);
    }

    public Completable addProduction(BakeryProduction production) {
        return dao.insertProduction(production.toProductionEntity());
    }

    public Completable updateProduction(BakeryProduction production) {
        return dao.updateProduction(production.toProductionEntity());
    }

    public Completable deleteProduction(int id) {
        return dao.deleteProduction(id);
    }

    private List<BakeryProduction> transformProductions(List<ProductionEntity> productionEntities) {
        List<BakeryProduction> data = new ArrayList<>();
        for (ProductionEntity productionEntity : productionEntities) {
            data.add(BakeryProduction.fromProductionEntity(productionEntity));
        }
        return data;
    }

}
