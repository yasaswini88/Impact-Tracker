package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;
import java.util.Map;

public class SeasonalTrendDto {
    private Long businessId;
    private Map<String, Number> monthlyTrends; // handles both Integer & Double
    private LocalDateTime generatedAt;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Map<String, Number> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(Map<String, Number> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
