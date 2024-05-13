package com.example.coursework.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.coursework.data.database.entities.IngredientEntity;
import com.example.coursework.data.database.entities.ProductEntity;
import com.example.coursework.data.database.entities.ProductIngredientCrossRef;
import com.example.coursework.data.database.entities.ProductWithIngredients;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        version = 1,
        entities = {
                IngredientEntity.class,
                ProductEntity.class,
                ProductIngredientCrossRef.class
        }
)
public abstract class AppDatabase extends RoomDatabase {
        private static final int NUMBER_OF_THREADS = 4;
        public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        public abstract ProductDao getProductDao();
}
