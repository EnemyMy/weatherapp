package com.example.app_18_weatherapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CurrentWeatherEntity.class, HourlyWeatherEntity.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    private static WeatherDatabase instance;

    public static synchronized WeatherDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), WeatherDatabase.class, "Weather Database")
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    public abstract CurrentWeatherDao getCurrentWeatherDao();
    public abstract HourlyWeatherDao getHourlyWeatherDao();
}
