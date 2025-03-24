package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "global_seasonal_trends")
public class GlobalSeasonalTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String businessType;

    @Column(columnDefinition = "TEXT")
    private String monthlyTrendsJson;

    private LocalDateTime generatedAt;

    @PrePersist
    void prePersist() {
        generatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getMonthlyTrendsJson() { return monthlyTrendsJson; }
    public void setMonthlyTrendsJson(String monthlyTrendsJson) { this.monthlyTrendsJson = monthlyTrendsJson; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
