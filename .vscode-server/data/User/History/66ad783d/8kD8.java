package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeatherForecastDto {

    private Long forecastId;
    private String cityName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String weatherCondition;
    private String severity;
    private Double chance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate currentDate;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    public WeatherForecastDto() {
    }

    public Long getForecastId() {
        return forecastId;
    }
    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }
    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
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
}
