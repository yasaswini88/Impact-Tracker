package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;

public class SentimentAnalysisDto {

    private Long id;
    private Long businessId; // We only store the businessId here
    private String clientPhoneNumber;
    private String sentiment;
    private String text;
    private String audioUrl;
    private LocalDateTime generatedAt;
    private LocalDateTime updatedAt;
    private Boolean aiHandled;


    // Default Constructor
    public SentimentAnalysisDto() {}

    // All-Args Constructor
    public SentimentAnalysisDto(Long id, Long businessId, String clientPhoneNumber, String sentiment, String text,
                                String audioUrl, LocalDateTime generatedAt, LocalDateTime updatedAt, Boolean aiHandled) {
        this.id = id;
        this.businessId = businessId;
        this.clientPhoneNumber = clientPhoneNumber;
        this.sentiment = sentiment;
        this.text = text;
        this.audioUrl = audioUrl;
        this.generatedAt = generatedAt;
        this.updatedAt = updatedAt;
        this.aiHandled = aiHandled;
    }

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

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getAiHandled() {
        return aiHandled;
    }

    public void setAiHandled(Boolean aiHandled) {
        this.aiHandled = aiHandled;
    }


    @Override
    public String toString() {
        return "SentimentAnalysisDto{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", sentiment='" + sentiment + '\'' +
                ", text='" + text + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", generatedAt=" + generatedAt +
                ", updatedAt=" + updatedAt +
                ", aiHandled=" + aiHandled +
                '}';
    }
}
