package com.Impact_Tracker.Impact_Tracker.DTO;

public class FeatureDto {

    private Long featureId;
    private String featureName;
    private String createdTime;

    public FeatureDto() {
    }

    // Getters and Setters
    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
