package com.weatherapp.weather_service.controller;

import com.weatherapp.weather_service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<String> getWeatherDetails(@RequestParam String city){
        String cityWeatherDetails = weatherService.getCityWeatherDetails(city);
        return new ResponseEntity<>(cityWeatherDetails, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public String getHourlyForecast(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getHourlyForecast(lat, lon);
    }
}
