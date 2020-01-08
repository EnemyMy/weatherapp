package com.example.app_18_weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {

    @GET("weather?units=metric&APPID=5a48cb7d3ad01612c6f15f2f1c6900da")
    Call<CurrentWeather> getCurrentWeather(@Query("lat") Double lat, @Query("lon") Double lon);

    @GET("forecast?units=metric&APPID=5a48cb7d3ad01612c6f15f2f1c6900da")
    Call<HourlyWeather> getHourlyWeather(@Query("lat") Double lat, @Query("lon") Double lon);
}
