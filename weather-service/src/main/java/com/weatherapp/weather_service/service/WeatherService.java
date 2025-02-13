package com.weatherapp.weather_service.service;

public interface WeatherService {
    String getCityWeatherDetails(String city);

    String getHourlyForecast(double lat, double lon);
}
