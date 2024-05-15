package com.example.coursework.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.data.database.entities.ProductionEntity;
import com.example.coursework.data.database.entities.SaleEntity;

@Database(
        version = 1,
        entities = {
                IngredientEntity.class,
                ProductEntity.class,
                ProductionEntity.class,
                SaleEntity.class
        }
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
        public abstract ProductDao getProductDao();
        public abstract ProductionDao getProductionDao();
        public abstract SaleDao getSaleDao();
}
