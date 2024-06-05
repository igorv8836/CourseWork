package com.example.coursework.data.repositories;


import com.example.coursework.App;
import com.example.coursework.data.database.SaleDao;
import com.example.coursework.data.database.entities.SaleEntity;
import com.example.coursework.domain.repositories.SalesRepository;
import com.example.coursework.ui.entities.ProductSale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class SalesRepositoryImpl implements SalesRepository {
    private final SaleDao dao;

    public SalesRepositoryImpl() {
        dao = App.getDb().getSaleDao();
    }

    @Override
    public Observable<List<ProductSale>> getSales() {
        return dao.getSales().map(this::transformSales);
    }

    @Override
    public Observable<ProductSale> getSale(int id) {
        return dao.getSale(id).map(ProductSale::fromSaleEntity);
    }

    @Override
    public Completable addSale(ProductSale sale) {
        return dao.insertSale(sale.toSaleEntity());
    }

    @Override
    public Completable updateSale(ProductSale sale) {
        return dao.updateSale(sale.toSaleEntity());
    }

    @Override
    public Completable deleteSale(int id) {
        return dao.deleteSale(id);
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
        return dao.getSalesByDate(startDate, endDate).map(this::transformSales);
    }

    @Override
    public Observable<Double> getRevenue(long startDate, long endDate) {
        return dao.getRevenue(startDate, endDate);
    }
}