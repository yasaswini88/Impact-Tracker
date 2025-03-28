package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class CallVolumeDto {
    private Long callVolumeId;
    private Long businessId; // reference to Business entity
    private List<String> months;
    private List<Integer> answered;
    private List<Integer> missed;
    private List<Integer> voicemail;
    private LocalDateTime dateCreated;

    // Getters and Setters

    public Long getCallVolumeId() {
        return callVolumeId;
    }

    public void setCallVolumeId(Long callVolumeId) {
        this.callVolumeId = callVolumeId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<Integer> getAnswered() {
        return answered;
    }

    public void setAnswered(List<Integer> answered) {
        this.answered = answered;
    }

    public List<Integer> getMissed() {
        return missed;
    }

    public void setMissed(List<Integer> missed) {
        this.missed = missed;
    }

    public List<Integer> getVoicemail() {
        return voicemail;
    }

    public void setVoicemail(List<Integer> voicemail) {
        this.voicemail = voicemail;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
