package com.example.app_18_weatherapp;

import com.google.gson.annotations.SerializedName;

public class HourlyWeather {

    private WeatherItem[] list;

    private City city;

    public HourlyWeather(WeatherItem[] list, City city) {
        this.list = list;
        this.city = city;
    }

    public WeatherItem[] getList() {
        return list;
    }

    public City getCity() {
        return city;
    }

    class WeatherItem {

        @SerializedName("main")
        private Temperature temperature;

        private Weather[] weather;

        @SerializedName("dt_txt")
        private String date;

        public WeatherItem(Temperature temperature, Weather[] weather, String date) {
            this.temperature = temperature;
            this.weather = weather;
            this.date = date;
        }

        public Temperature getTemperature() {
            return temperature;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public String getDate() {
            return date;
        }
    }

    class Temperature {
        private float temp_max;

        public Temperature(float temp_max) {
            this.temp_max = temp_max;
        }

        public float getTemp_max() {
            return temp_max;
        }
    }

    class Weather {
        private String icon;

        public Weather(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }
    }

    class City {
        private String name;

        public City(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
