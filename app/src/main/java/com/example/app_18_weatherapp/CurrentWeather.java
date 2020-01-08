package com.example.app_18_weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentWeather {

    @Expose
    @SerializedName("name")
    private String city;

    @SerializedName("main")
    private Temperature temperature;

    private Weather[] weather;

    private Wind wind;

    public CurrentWeather(String city, Temperature temperature, Weather[] weather, Wind wind) {
        this.city = city;
        this.temperature = temperature;
        this.weather = weather;
        this.wind = wind;
    }

    public String getCity() {
        return city;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    class Temperature {
        private float temp;
        private int pressure;

        public Temperature(float temp, int pressure) {
            this.temp = temp;
            this.pressure = pressure;
        }

        public float getTemp() {
            return temp;
        }

        public int getPressure() {
            return pressure;
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

    class Wind {
        private float speed;

        public Wind(int speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }
    }
}
