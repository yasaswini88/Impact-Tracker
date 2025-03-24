package com.Impact_Tracker.Impact_Tracker.DTO;

public class PlansDto {

    private Long planId;
    private String planName;
    private String createdTime;   // As String in ISO format
    private String updatedTime;   // As String in ISO format

    // Constructors
    public PlansDto() {
    }

    // Getters and Setters
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
