package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeatherForecastDto {

    private String cityName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String weatherCondition;
    private String severity;
    private Double chance; // e.g., 60.0 for 60%
    private LocalDate forecastDate;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    // Constructors
    public WeatherForecastDTO() {}

    public WeatherForecastDTO(String cityName, LocalDateTime startTime, LocalDateTime endTime, String weatherCondition, 
                              String severity, Double chance, LocalDate forecastDate, String zipCode, 
                              Double latitude, Double longitude) {
        this.cityName = cityName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weatherCondition = weatherCondition;
        this.severity = severity;
        this.chance = chance;
        this.forecastDate = forecastDate;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public LocalDate getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(LocalDate forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "WeatherForecastDTO{" +
                "cityName='" + cityName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", severity='" + severity + '\'' +
                ", chance=" + chance +
                ", forecastDate=" + forecastDate +
                ", zipCode='" + zipCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
