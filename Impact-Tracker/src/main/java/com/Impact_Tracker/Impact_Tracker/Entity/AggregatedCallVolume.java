package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aggregated_call_volume")
public class AggregatedCallVolume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g., "Hotel"
    @Column(nullable = false)
    private String businessType;

    // e.g., "small"
    @Column(nullable = false)
    private String businessSize;

    // The month for which the average is calculated (e.g., "January")
    @Column(nullable = false)
    private String month;

    // Average call counts across matching records
    private Double avgAnswered;
    private Double avgMissed;
    private Double avgVoicemail;

    // When the aggregation was performed
    private LocalDateTime aggregatedAt;

    // Default constructor
    public AggregatedCallVolume() {
    }

    // Parameterized constructor
    public AggregatedCallVolume(String businessType, String businessSize, String month,
                                Double avgAnswered, Double avgMissed, Double avgVoicemail,
                                LocalDateTime aggregatedAt) {
        this.businessType = businessType;
        this.businessSize = businessSize;
        this.month = month;
        this.avgAnswered = avgAnswered;
        this.avgMissed = avgMissed;
        this.avgVoicemail = avgVoicemail;
        this.aggregatedAt = aggregatedAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessSize() {
        return businessSize;
    }

    public void setBusinessSize(String businessSize) {
        this.businessSize = businessSize;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAvgAnswered() {
        return avgAnswered;
    }

    public void setAvgAnswered(Double avgAnswered) {
        this.avgAnswered = avgAnswered;
    }

    public Double getAvgMissed() {
        return avgMissed;
    }

    public void setAvgMissed(Double avgMissed) {
        this.avgMissed = avgMissed;
    }

    public Double getAvgVoicemail() {
        return avgVoicemail;
    }

    public void setAvgVoicemail(Double avgVoicemail) {
        this.avgVoicemail = avgVoicemail;
    }

    public LocalDateTime getAggregatedAt() {
        return aggregatedAt;
    }

    public void setAggregatedAt(LocalDateTime aggregatedAt) {
        this.aggregatedAt = aggregatedAt;
    }

    @Override
    public String toString() {
        return "AggregatedCallVolume{" +
                "id=" + id +
                ", businessType='" + businessType + '\'' +
                ", businessSize='" + businessSize + '\'' +
                ", month='" + month + '\'' +
                ", avgAnswered=" + avgAnswered +
                ", avgMissed=" + avgMissed +
                ", avgVoicemail=" + avgVoicemail +
                ", aggregatedAt=" + aggregatedAt +
                '}';
    }
}
