package com.example.app_18_weatherapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HourlyWeatherEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float temperature;
    private String weatherIcon;
    private String date;
    private String city;

    public HourlyWeatherEntity(float temperature, String weatherIcon, String date, String city) {
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.date = date;
        this.city = city;
    }

    public HourlyWeatherEntity(HourlyWeather.WeatherItem weatherItem, String city) {
        this.temperature = weatherItem.getTemperature().getTemp_max();
        this.weatherIcon = weatherItem.getWeather()[0].getIcon();
        this.date = weatherItem.getDate();
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public void setId(int id) {
        this.id = id;
    }
}
