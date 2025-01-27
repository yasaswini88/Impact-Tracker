package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.WeatherForecastDto;
import com.Impact_Tracker.Impact_Tracker.Entity.WeatherForecast;
import com.Impact_Tracker.Impact_Tracker.Repo.WeatherForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class WeatherForecastService {

    @Autowired
    private WeatherForecastRepository weatherForecastRepository;

    // Optionally configure this as a bean if you prefer
    private RestTemplate restTemplate = new RestTemplate();

    // The OpenWeatherMap URL (hard-coded for example)
    private static final String WEATHER_API_URL =
        "https://api.openweathermap.org/data/3.0/onecall?lat=38.95949&lon=-77.60829&appid=7a18a0da769c3d539b9305e0f8983a7e";

    /**
     * Scheduled job that runs every 15 minutes.
     * Cron format: second minute hour day-of-month month day-of-week
     * "0 0/15 * * * ?" means every 15 minutes.
     */
    // @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron="0 1 1 * * *")
    public void fetchAndStoreForecast() {
        // 1) Call the API
        Map<String, Object> responseMap = restTemplate.getForObject(WEATHER_API_URL, Map.class);
        if (responseMap == null) {
            System.out.println("No data returned from Weather API.");
            return;
        }

        // 2) Parse the needed fields
        try {
            Double lat = (Double) responseMap.get("lat");
            Double lon = (Double) responseMap.get("lon");

            // By default, One Call doesn't return a city name, just "timezone".
            String timezone = (String) responseMap.get("timezone");
            String cityName = (timezone != null) ? timezone : "Unknown";

            // Current weather block
            Map<String, Object> currentData = (Map<String, Object>) responseMap.get("current");
            if (currentData != null) {
                // "weather" is an array
                String weatherCondition = null;
                if (currentData.containsKey("weather")) {
                    var weatherList = (Iterable<Object>) currentData.get("weather");
                    for (Object w : weatherList) {
                        Map<String, Object> wMap = (Map<String, Object>) w;
                        weatherCondition = (String) wMap.get("description");
                        break; // just take the first entry
                    }
                }

                // Possibly interpret 'clouds' or 'pop' as "chance"
                Double chance = null;
                if (currentData.containsKey("clouds")) {
                    chance = ((Number) currentData.get("clouds")).doubleValue();
                }

                // Example severity logic
                String severity = "Medium";
                if (chance != null && chance > 80) {
                    severity = "High";
                }

                // 3) Build a WeatherForecast entity
                WeatherForecast forecast = new WeatherForecast();
                forecast.setLatitude(lat);
                forecast.setLongitude(lon);
                forecast.setCityName(cityName);
                forecast.setWeatherCondition(weatherCondition);
                forecast.setChance(chance);
                forecast.setSeverity(severity);

                // Using a String for aiHandled:
                forecast.setAiHandled("false");

                // Convert Unix seconds to LocalDateTime if present
                if (currentData.containsKey("dt")) {
                    Long dt = ((Number) currentData.get("dt")).longValue();
                    LocalDateTime dateTime = convertUnixToLocalDateTime(dt);
                    forecast.setStartTime(dateTime);
                }

                // Optionally set an endTime or skip
                forecast.setEndTime(null);

                // Set forecastDate as today's date
                forecast.setForecastDate(LocalDate.now());

                // Setup created/updated timestamps
                forecast.setCreatedAt(LocalDateTime.now());
                forecast.setUpdatedAt(LocalDateTime.now());

                // Hard-coded zip code for example
                forecast.setZipCode("20105");

                // 4) Save to DB
                weatherForecastRepository.save(forecast);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error parsing weather API response: " + ex.getMessage());
        }
    }

    /**
     * Example method to convert a unix timestamp (seconds) into LocalDateTime
     */
    private LocalDateTime convertUnixToLocalDateTime(Long unixSeconds) {
        return java.time.Instant.ofEpochSecond(unixSeconds)
                .atZone(java.time.ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    /**
     * Optional method: If you want a direct way to fetch & parse without scheduling
     */
    public WeatherForecastDto fetchForecastNow() {
        Map<String, Object> responseMap = restTemplate.getForObject(WEATHER_API_URL, Map.class);
        if (responseMap == null) {
            return null;
        }
        // Minimal parse for a DTO
        WeatherForecastDto dto = new WeatherForecastDto();
        // ...
        return dto;
    }

    /**
     * Retrieve all forecasts from DB (for your controller, if you want)
     */
    public java.util.List<WeatherForecast> getAllForecasts() {
        return weatherForecastRepository.findAll();
    }
}
