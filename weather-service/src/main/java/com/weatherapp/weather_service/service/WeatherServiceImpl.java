package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.WeatherClient;
import com.weatherapp.weather_service.exception.BadRequestException;
import com.weatherapp.weather_service.exception.CityNotFoundException;
import com.weatherapp.weather_service.exception.InvalidCoordinatesException;
import com.weatherapp.weather_service.exception.WeatherClientException;
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
        if (city == null || city.trim().isEmpty()) {
            throw new BadRequestException("City cannot be empty.");
        }
        try {
            return weatherClient.getWeather(city, apiKey, "metric");
        } catch (FeignException.NotFound exception) {
            throw new CityNotFoundException("City not found. Please enter a valid city.");
        } catch (FeignException.BadRequest exception) {
            throw new BadRequestException("Invalid city format. Please enter a valid city name.");
        } catch (FeignException e) {
            throw new WeatherClientException("An error occurred while fetching weather data.");
        }
    }

    @Override
    public String getHourlyForecast(double lat, double lon) {
        try {
            if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                throw new InvalidCoordinatesException("Invalid latitude or longitude values.");
            }
            return weatherClient.getHourlyForecast(lat, lon, apiKey, "metric");
        } catch (FeignException.NotFound e) {
            throw new CityNotFoundException("Location not found for given coordinates.");
        } catch (FeignException.BadRequest e) {
            throw new InvalidCoordinatesException("Invalid coordinates provided.");
        } catch (FeignException e) {
            throw new WeatherClientException("Error fetching hourly forecast.");
        }
    }
}
