package com.example.coursework.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursework.data.database.entities.ProductionEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface ProductionDao {
    @Insert
    Completable insertProduction(ProductionEntity production);

    @Update
    Completable updateProduction(ProductionEntity production);

    @Query("DELETE FROM production WHERE id = :id")
    Completable deleteProduction(String id);

    @Query("SELECT * FROM production WHERE id = :id")
    Observable<ProductionEntity> getProduction(String id);

    @Query("SELECT * FROM production")
    Observable<List<ProductionEntity>> getAllProductions();

    @Query("SELECT * FROM production WHERE endTime >= :startDate AND endTime <= :endDate")
    Observable<List<ProductionEntity>> getProductionsByDate(long startDate, long endDate);
}
