package com.example.app_18_weatherapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentWeatherDao {

    @Insert(onConflict = REPLACE)
    void insert(CurrentWeatherEntity entity);

    @Query("DELETE FROM CurrentWeatherEntity")
    void delete();

    @Query("SELECT * FROM CurrentWeatherEntity WHERE id = 1")
    LiveData<CurrentWeatherEntity> get();
}
