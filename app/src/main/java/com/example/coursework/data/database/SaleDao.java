package com.example.coursework.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursework.data.database.entities.SaleEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface SaleDao {
    @Insert
    Completable insertSale(SaleEntity saleEntity);

    @Insert
    Completable insertSales(List<SaleEntity> saleEntities);

    @Update
    Completable updateSale(SaleEntity saleEntity);

    @Query("DELETE FROM sales WHERE id = :id")
    Completable deleteSale(String id);

    @Query("DELETE FROM sales")
    Completable deleteSales();

    @Query("SELECT * FROM sales WHERE id = :id")
    Observable<SaleEntity> getSale(String id);

    @Query("SELECT * FROM sales")
    Observable<List<SaleEntity>> getSales();

    @Query("SELECT SUM(salePrice * count) FROM sales WHERE saleDate >= :startDate AND saleDate <= :endDate")
    Observable<Double> getRevenue(long startDate, long endDate);

    @Query("SELECT * FROM sales WHERE saleDate >= :startDate AND saleDate <= :endDate")
    Observable<List<SaleEntity>> getSalesByDate(long startDate, long endDate);
}
