// File: com/Impact_Tracker/Impact_Tracker/DTO/AggregatedCallVolumeDto.java
package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;

public class AggregatedCallVolumeDto {
    private Long id;
    private String businessType;
    private String businessSize;
    private String month;
    private Double avgAnswered;
    private Double avgMissed;
    private Double avgVoicemail;
    private LocalDateTime aggregatedAt;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getBusinessSize() { return businessSize; }
    public void setBusinessSize(String businessSize) { this.businessSize = businessSize; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Double getAvgAnswered() { return avgAnswered; }
    public void setAvgAnswered(Double avgAnswered) { this.avgAnswered = avgAnswered; }

    public Double getAvgMissed() { return avgMissed; }
    public void setAvgMissed(Double avgMissed) { this.avgMissed = avgMissed; }

    public Double getAvgVoicemail() { return avgVoicemail; }
    public void setAvgVoicemail(Double avgVoicemail) { this.avgVoicemail = avgVoicemail; }

    public LocalDateTime getAggregatedAt() { return aggregatedAt; }
    public void setAggregatedAt(LocalDateTime aggregatedAt) { this.aggregatedAt = aggregatedAt; }
}
