package com.example.app_18_weatherapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HourlyWeatherDao {

    @Insert
    void insert(HourlyWeatherEntity entity);

    @Query("DELETE FROM HourlyWeatherEntity")
    void deleteAll();

    @Query("SELECT * FROM HourlyWeatherEntity")
    LiveData<List<HourlyWeatherEntity>> getAll();

}
