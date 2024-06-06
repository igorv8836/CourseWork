package com.example.coursework.domain.repositories;


import com.example.coursework.ui.entities.BakeryProduction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface ProductionRepository {
    Observable<List<BakeryProduction>> getProductions();
    Observable<BakeryProduction> getProduction(String id);
    Completable addProduction(BakeryProduction production);
    Completable updateProduction(BakeryProduction production);
    Completable deleteProduction(String id);
    Observable<List<BakeryProduction>> getProductionsByDate(long startDate, long endDate);
}
