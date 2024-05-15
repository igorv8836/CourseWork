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

public class SalesRepository {
    private final SaleDao dao;

    public SalesRepository() {
        dao = App.getDb().getSaleDao();
    }

    public Observable<List<ProductSale>> getSales() {
        return dao.getSales().map(this::transformSales);
    }

    public Observable<ProductSale> getSale(int id) {
        return dao.getSale(id).map(ProductSale::fromSaleEntity);
    }

    public Completable addSale(ProductSale sale) {
        return dao.insertSale(sale.toSaleEntity());
    }

    public Completable updateSale(ProductSale sale) {
        return dao.updateSale(sale.toSaleEntity());
    }

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

}
