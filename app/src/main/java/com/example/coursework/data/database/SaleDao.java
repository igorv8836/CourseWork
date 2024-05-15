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

    @Update
    Completable updateSale(SaleEntity saleEntity);

    @Query("DELETE FROM sales WHERE id = :id")
    Completable deleteSale(int id);

    @Query("SELECT * FROM sales WHERE id = :id")
    Observable<SaleEntity> getSale(int id);

    @Query("SELECT * FROM sales")
    Observable<List<SaleEntity>> getSales();
}
