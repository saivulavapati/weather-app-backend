package com.weatherapp.weather_service.controller;

import com.weatherapp.weather_service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<?> getWeatherDetails(@RequestParam(required = false) String city){
        String cityWeatherDetails = weatherService.getCityWeatherDetails(city);
        return new ResponseEntity<>(cityWeatherDetails, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public ResponseEntity<String> getHourlyForecast(@RequestParam double lat, @RequestParam double lon) {
        String forecast = weatherService.getHourlyForecast(lat, lon);
        return ResponseEntity.ok(forecast);
    }
}
