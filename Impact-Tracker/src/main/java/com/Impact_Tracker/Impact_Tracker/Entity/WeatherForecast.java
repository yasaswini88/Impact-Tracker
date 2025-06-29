package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_forecast")
public class WeatherForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forecastId;

    @Column
    private String cityName;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String weatherCondition;

    @Column
    private String severity;

    @Column
    private Double chance; // e.g., 60.0 for 60%

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(name = "forecast_date")
    private LocalDate forecastDate;

    @Column
    private String zipCode;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = true)
    private String aiHandled = "false";

    // Constructors
    public WeatherForecast() {}

    // ---------------------------
    // NOTE: Original constructor had Boolean aiHandled,
    // but the field is a String. We'll keep this line exactly
    // but convert Boolean -> String to avoid compile error.
    // ---------------------------
    public WeatherForecast(String cityName, LocalDateTime startTime, LocalDateTime endTime, String weatherCondition,
                           String severity, Double chance, LocalDateTime createdAt, LocalDateTime updatedAt,
                           LocalDate forecastDate, String zipCode, Double latitude, Double longitude, Boolean aiHandled) {
        this.cityName = cityName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weatherCondition = weatherCondition;
        this.severity = severity;
        this.chance = chance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.forecastDate = forecastDate;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        // this.aiHandled = aiHandled; // (Original line - causes error if field is String)
        this.aiHandled = String.valueOf(aiHandled); // (Fixed line)
    }

    // Getters and Setters
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

    public String getAiHandled() {
        return aiHandled;
    }

    public void setAiHandled(String aiHandled) {
        this.aiHandled = aiHandled;
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "forecastId=" + forecastId +
                ", cityName='" + cityName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", severity='" + severity + '\'' +
                ", chance=" + chance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", forecastDate=" + forecastDate +
                ", zipCode='" + zipCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", aiHandled=" + aiHandled +
                '}';
    }
}
