package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seasonal_trends")
public class SeasonalTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(columnDefinition = "TEXT")
    private String monthlyTrendsJson; // JSON string containing trends month-wise

    private LocalDateTime generatedAt;

    @PrePersist
    void prePersist() {
        generatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getMonthlyTrendsJson() {
        return monthlyTrendsJson;
    }

    public void setMonthlyTrendsJson(String monthlyTrendsJson) {
        this.monthlyTrendsJson = monthlyTrendsJson;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
