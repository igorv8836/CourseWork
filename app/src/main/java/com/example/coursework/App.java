package com.example.coursework;

import android.app.Application;

import androidx.room.Room;

import com.example.coursework.data.database.AppDatabase;

public class App extends Application {
    static App instance;
    static AppDatabase db;

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDb() {
        return db;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(this, AppDatabase.class,"mainDatabase").build();
    }
}
