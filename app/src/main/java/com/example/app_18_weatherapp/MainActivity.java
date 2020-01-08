package com.example.app_18_weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements ToggleWeatherViewFragment.OnFragmentInteractionListener{

    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 123;
    private Location location = new Location("emptyLocation");
    private WeatherViewModel viewModel;
    private LiveData<CurrentWeatherEntity> currentWeatherEntity;
    private LiveData<List<HourlyWeatherEntity>> hourlyWeatherEntities;
    private Observer<CurrentWeatherEntity> currentWeatherObserver = new Observer<CurrentWeatherEntity>() {
        @Override
        public void onChanged(CurrentWeatherEntity entity) {
            if (getSupportFragmentManager().findFragmentByTag("toggle_weather_view_fragment").getChildFragmentManager().findFragmentByTag("current_weather_fragment") != null)
                updateFragmentLiveData("current_weather_fragment");
        }
    };

    private Observer<List<HourlyWeatherEntity>> hourlyWeatherObserver = new Observer<List<HourlyWeatherEntity>>() {
        @Override
        public void onChanged(List<HourlyWeatherEntity> hourlyWeatherEntities) {
            if (getSupportFragmentManager().findFragmentByTag("toggle_weather_view_fragment").getChildFragmentManager().findFragmentByTag("hourly_weather_fragment") != null)
                updateFragmentLiveData("hourly_weather_fragment");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.toggle_weather_fragment_holder, new BlankFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitNow();
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        getCurrentLocation();
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_activity_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                location = new Location("emptyLocation");
                getCurrentLocation();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCurrentLocation();
    }

    @AfterPermissionGranted(ACCESS_FINE_LOCATION_REQUEST_CODE)
    private void getCurrentLocation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        MainActivity.this.location.setLatitude(location.getLatitude());
                        MainActivity.this.location.setLongitude(location.getLongitude());
                        MainActivity.this.location.setProvider("currentLocationUpdated");
                        currentWeatherEntity = viewModel.updateCurrentWeatherEntity(location.getLatitude(), location.getLongitude());
                        hourlyWeatherEntities = viewModel.updateHourlyWeatherEntities(location.getLatitude(), location.getLongitude());
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.toggle_weather_fragment_holder, new ToggleWeatherViewFragment(), "toggle_weather_view_fragment")
                                .commitNow();
                        currentWeatherEntity.observe(MainActivity.this, currentWeatherObserver);
                        hourlyWeatherEntities.observe(MainActivity.this, hourlyWeatherObserver);
                    }
                    else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Can't get geolocation")
                                .setMessage("This app can't run without information about your location")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    }
                }
            });
        }
        else {
            if (EasyPermissions.permissionPermanentlyDenied(this, Manifest.permission.ACCESS_FINE_LOCATION))
                new AppSettingsDialog.Builder(this).setRequestCode(this.ACCESS_FINE_LOCATION_REQUEST_CODE).build().show();
            else
                EasyPermissions.requestPermissions(this, "This permission required for proper functionality of this application",
                        ACCESS_FINE_LOCATION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.ACCESS_FINE_LOCATION_REQUEST_CODE)
            getCurrentLocation();
    }

    @Override
    public void updateFragmentLiveData(String fragmentTag) {
        ToggleWeatherViewFragment toggleFragment = (ToggleWeatherViewFragment) getSupportFragmentManager().findFragmentByTag("toggle_weather_view_fragment");
        if (fragmentTag.equals("current_weather_fragment") && currentWeatherEntity.getValue() != null){
            CurrentWeatherFragment currentWeatherFragment = (CurrentWeatherFragment) toggleFragment.getChildFragmentManager().findFragmentByTag(fragmentTag);
            TextView city = currentWeatherFragment.getView().findViewById(R.id.current_weather_fragment_current_city);
            TextView temperature = currentWeatherFragment.getView().findViewById(R.id.current_weather_fragment_temperature);
            TextView pressure = currentWeatherFragment.getView().findViewById(R.id.current_weather_fragment_pressure);
            ImageView weatherIcon = currentWeatherFragment.getView().findViewById(R.id.current_weather_icon);
            TextView windSpeed = currentWeatherFragment.getView().findViewById(R.id.current_weather_fragment_wind);
            CurrentWeatherEntity currentWeather = currentWeatherEntity.getValue();
            city.setText(currentWeather.getCity());
            temperature.setText(new StringBuilder().append(currentWeather.getTemperature()).append("°"));
            pressure.setText(new StringBuilder().append(currentWeather.getPressure()).append(" hpa"));
            weatherIcon.setBackgroundResource(this.getResources().getIdentifier("mipmap/ic_" + currentWeather.getWeatherIcon(), null, this.getPackageName()));
            windSpeed.setText(new StringBuilder().append(currentWeather.getWindSpeed()).append(" m/s"));
        }
        else if (fragmentTag.equals("hourly_weather_fragment") && hourlyWeatherEntities.getValue() != null){
            HourlyWeatherFragment hourlyWeatherFragment = (HourlyWeatherFragment) toggleFragment.getChildFragmentManager().findFragmentByTag(fragmentTag);
            ArrayList<HourlyWeatherEntity> hourlyWeather = (ArrayList<HourlyWeatherEntity>) hourlyWeatherEntities.getValue();
            ((TextView)hourlyWeatherFragment.getView().findViewById(R.id.hourly_weather_fragment_current_city)).setText(hourlyWeather.get(0).getCity());
            hourlyWeatherFragment.adapter.updateRecycler(hourlyWeather);
        }
    }
}
