package com.example.app_18_weatherapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static CurrentWeatherDao currentWeatherDao;
    private static HourlyWeatherDao hourlyWeatherDao;
    private static LiveData<CurrentWeatherEntity> currentWeatherEntity;
    private static LiveData<List<HourlyWeatherEntity>> hourlyWeatherEntities;
    private static OpenWeatherMapApi openWeatherMapApi;

    public Repository(Application application) {
        WeatherDatabase database = WeatherDatabase.getInstance(application);
        currentWeatherDao = database.getCurrentWeatherDao();
        hourlyWeatherDao = database.getHourlyWeatherDao();
        currentWeatherEntity = currentWeatherDao.get();
        hourlyWeatherEntities = hourlyWeatherDao.getAll();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
    }

    public LiveData<CurrentWeatherEntity> updateCurrentWeatherEntity(double latitude, double longitude) {
        openWeatherMapApi.getCurrentWeather(latitude, longitude).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new DeleteCurrentWeatherEntityAsyncTask().execute();
                    new InsertCurrentWeatherEntityAsyncTask().execute(new CurrentWeatherEntity(response.body()));
                }
                else
                    Log.i("updCurrentWeatherEntity", "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Log.i("updCurrentWeatherEntity", "onFailure: " + t.getMessage());
            }
        });
        return currentWeatherEntity;
    }

    public LiveData<List<HourlyWeatherEntity>> updateHourlyWeatherEntities(double latitude, double longitude) {
        openWeatherMapApi.getHourlyWeather(latitude, longitude).enqueue(new Callback<HourlyWeather>() {
            @Override
            public void onResponse(Call<HourlyWeather> call, Response<HourlyWeather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new DeleteAllHourlyWeatherEntityAsyncTask().execute();
                    new InsertHourlyWeatherEntityAsyncTask().execute(response.body());
                }
                else
                    Log.i("updHourlyWeatherEntity", "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<HourlyWeather> call, Throwable t) {
                Log.i("updHourlyWeatherEntity", "onFailure: " + t.getMessage());
            }
        });
        return hourlyWeatherEntities;
    }

    static class InsertCurrentWeatherEntityAsyncTask extends AsyncTask<CurrentWeatherEntity, Void, Void> {

        @Override
        protected Void doInBackground(CurrentWeatherEntity... currentWeatherEntities) {
            currentWeatherDao.insert(currentWeatherEntities[0]);
            return null;
        }
    }

    static class DeleteCurrentWeatherEntityAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            currentWeatherDao.delete();
            return null;
        }
    }

    static class InsertHourlyWeatherEntityAsyncTask extends AsyncTask<HourlyWeather, Void, Void> {

        @Override
        protected Void doInBackground(HourlyWeather... hourlyWeatherEntities) {
            String city = hourlyWeatherEntities[0].getCity().getName();
            for (int i = 0; i < hourlyWeatherEntities[0].getList().length; i++) {
                hourlyWeatherDao.insert(new HourlyWeatherEntity(hourlyWeatherEntities[0].getList()[i], city));
            }
            return null;
        }
    }

    static class DeleteAllHourlyWeatherEntityAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            hourlyWeatherDao.deleteAll();
            return null;
        }
    }
}
