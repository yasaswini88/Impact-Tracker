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
    @Scheduled(cron = "0 0/15 * * * ?")
    public void fetchAndStoreForecast() {
        // 1) Call the API
        Map<String, Object> responseMap = restTemplate.getForObject(WEATHER_API_URL, Map.class);
        if (responseMap == null) {
            System.out.println("No data returned from Weather API.");
            return;
        }

        // 2) Parse the needed fields
        //    - lat, lon
        //    - weatherCondition (from current.weather[0].description)
        //    - cityName (In One Call 3.0, "cityName" isn't returned, so you might store "timezone" or something else.)
        //    - chance, severity? (No direct field for severity, so this is up to your logic. 
        //      chance might come from "pop" if you read 'hourly[0].pop', or 'current.clouds' as a stand-in.)
        try {
            Double lat = (Double) responseMap.get("lat");
            Double lon = (Double) responseMap.get("lon");

            // By default, One Call doesn't return a city name, just "timezone".
            // We'll store the "timezone" into cityName as an example.
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
                // (pop is often 0 to 1, so you might multiply by 100 for a percentage)
                Double chance = null;
                if (currentData.containsKey("clouds")) {
                    // for example, let's store "clouds" as chance, e.g. 100 = 100% cloud cover
                    chance = ((Number) currentData.get("clouds")).doubleValue();
                }

                // No direct "severity" field in the response. 
                // You might define logic, e.g. if clouds > 80 => "High," else "Medium," etc.
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
                forecast.setAiHandled("false");

                // Store the "current.dt" as startTime, for example
                if (currentData.containsKey("dt")) {
                    Long dt = ((Number) currentData.get("dt")).longValue();
                    // dt is in *unix seconds*, so convert to LocalDateTime
                    LocalDateTime dateTime = convertUnixToLocalDateTime(dt);
                    forecast.setStartTime(dateTime);
                }
                // Optionally set an arbitrary endTime (e.g. startTime + 3 hours)
                // or skip endTime altogether
                forecast.setEndTime(null);

                // Set forecastDate as today's date
                forecast.setForecastDate(LocalDate.now());

                // Setup created/updated timestamps
                forecast.setCreatedAt(LocalDateTime.now());
                forecast.setUpdatedAt(LocalDateTime.now());

                // Zip code, if you have one (maybe you store a default?)
                forecast.setZipCode("20105");  // Hard-coded for example

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
        // Usually you'd use ZoneOffset, but let's assume UTC for example:
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
        // Do minimal parse and return a DTO (not saved)
        WeatherForecastDto dto = new WeatherForecastDto();
        // ... fill it ...
        return dto;
    }

    /**
     * Retrieve all forecasts from DB (for your controller, if you want)
     */
    public java.util.List<WeatherForecast> getAllForecasts() {
        return weatherForecastRepository.findAll();
    }

}
