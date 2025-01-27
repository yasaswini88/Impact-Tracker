package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.WeatherForecast;
import com.Impact_Tracker.Impact_Tracker.Service.WeatherForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather-forecasts")
public class WeatherForecastController {

    @Autowired
    private WeatherForecastService weatherForecastService;

    
    @GetMapping
    public ResponseEntity<List<WeatherForecast>> getAllForecasts() {
        List<WeatherForecast> forecasts = weatherForecastService.getAllForecasts();
        return ResponseEntity.ok(forecasts);
    }

   
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchNow() {
        weatherForecastService.fetchAndStoreForecast();
        return ResponseEntity.ok("Forecast fetched and stored!");
    }
}
