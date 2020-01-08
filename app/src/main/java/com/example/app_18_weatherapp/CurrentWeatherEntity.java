package com.example.app_18_weatherapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrentWeatherEntity {

    @PrimaryKey
    private int id = 1;

    private String city;
    private int temperature;
    private int pressure;
    private String weatherIcon;
    private float windSpeed;

    public CurrentWeatherEntity(String city, int temperature, int pressure, String weatherIcon, float windSpeed) {
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.weatherIcon = weatherIcon;
        this.windSpeed = windSpeed;
    }

    public CurrentWeatherEntity(CurrentWeather currentWeather) {
        this.city = currentWeather.getCity();
        this.temperature = Math.round(currentWeather.getTemperature().getTemp());
        this.pressure = currentWeather.getTemperature().getPressure();
        this.weatherIcon = currentWeather.getWeather()[0].getIcon();
        this.windSpeed = currentWeather.getWind().getSpeed();
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setId(int id) {
        this.id = id;
    }
}
