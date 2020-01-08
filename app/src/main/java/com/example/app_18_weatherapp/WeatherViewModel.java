package com.example.app_18_weatherapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private Repository repository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
    }

    public LiveData<CurrentWeatherEntity> updateCurrentWeatherEntity(double latitude, double longitude) {
        return repository.updateCurrentWeatherEntity(latitude, longitude);
    }

    public LiveData<List<HourlyWeatherEntity>> updateHourlyWeatherEntities(double latitude, double longitude) {
        return repository.updateHourlyWeatherEntities(latitude, longitude);
    }
}
