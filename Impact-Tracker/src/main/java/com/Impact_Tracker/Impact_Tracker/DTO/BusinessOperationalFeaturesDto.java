package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDate;

public class BusinessOperationalFeaturesDto {

    private Long id;
    private Long businessId;
    private Long featureId;
    private LocalDate featureEnabled;

    // Constructors
    public BusinessOperationalFeaturesDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public LocalDate getFeatureEnabled() {
        return featureEnabled;
    }

    public void setFeatureEnabled(LocalDate featureEnabled) {
        this.featureEnabled = featureEnabled;
    }
}
