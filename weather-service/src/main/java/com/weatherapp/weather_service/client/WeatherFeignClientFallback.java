package com.weatherapp.weather_service.client;

import org.springframework.stereotype.Component;

@Component
public class WeatherFeignClientFallback implements WeatherClient{
    @Override
    public String getWeather(String city, String apiKey, String units) {
        return "Weather data is not available";
    }

    @Override
    public String getHourlyForecast(double lat, double lon, String apiKey, String units) {
        return "Weather data is not available";
    }
}
