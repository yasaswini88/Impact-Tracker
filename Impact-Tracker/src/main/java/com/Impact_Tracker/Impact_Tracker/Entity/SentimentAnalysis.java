package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sentiment_analysis")
public class SentimentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "client_phone_number")
    private String clientPhoneNumber;

    @Column(name = "sentiment")
    private String sentiment;

    // Store the text of the transcript or relevant text
    @Column(name = "analysis_text", length = 2000)
    private String text;

    // Large string for audio URL
    @Column(name = "audio_url", length = 2000)
    private String audioUrl;

    @Column(name = "generated_at", nullable = true)
    private LocalDateTime generatedAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "ai_handled")
private Boolean aiHandled = false; 

    // Constructors
    public SentimentAnalysis() {
    }

    public SentimentAnalysis(Business business, String clientPhoneNumber, String sentiment, String text, String audioUrl, Boolean aiHandled) {
        this.business = business;
        this.clientPhoneNumber = clientPhoneNumber;
        this.sentiment = sentiment;
        this.text = text;
        this.audioUrl = audioUrl;
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.aiHandled = false;
    }

    // Lifecycle Callbacks to set timestamps automatically
    @PrePersist
    public void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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



}