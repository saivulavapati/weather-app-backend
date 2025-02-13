package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.WeatherClient;
import com.weatherapp.weather_service.exception.CityNotFoundException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Value("${openweather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherClient weatherClient;
    @Override
    public String getCityWeatherDetails(String city) {
        try{
            return weatherClient.getWeather(city, apiKey, "metric");
        }catch (FeignException.NotFound exception){
            throw new CityNotFoundException("City Not Found");
        }
    }

    @Override
    public String getHourlyForecast(double lat, double lon) {
        try {
            return weatherClient.getHourlyForecast(lat, lon, apiKey, "metric");
        } catch (FeignException e) {
            throw new RuntimeException("Error fetching forecast", e);
        }
    }
}
