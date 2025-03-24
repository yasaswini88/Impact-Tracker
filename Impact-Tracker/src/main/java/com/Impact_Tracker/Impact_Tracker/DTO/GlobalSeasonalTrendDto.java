package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;
import java.util.Map;

public class GlobalSeasonalTrendDto {

    private String businessType;
    private Map<String, Number> monthlyTrends;
    private LocalDateTime generatedAt;

    // Getters and Setters
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public Map<String, Number> getMonthlyTrends() { return monthlyTrends; }
    public void setMonthlyTrends(Map<String, Number> monthlyTrends) { this.monthlyTrends = monthlyTrends; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}