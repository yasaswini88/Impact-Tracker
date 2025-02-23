package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * This table will store each row of a business choosing a single strategy,
 * with optional fields for call campaign voice, target audience, etc.
 */
@Entity
@Table(name = "business_call_campaign_strategy_selection")
public class BusinessCallCampaignStrategySelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne => a single business can have many selected strategies
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // ManyToOne => each row references exactly one strategy
    @ManyToOne
    @JoinColumn(name = "call_campaign_strategy_id", nullable = false)
    private CallCampaignStrategies callCampaignStrategy;

    // For the business’s custom message or “voice”
    @Column(name = "call_campaign_voice", length = 500)
    private String callCampaignVoice;

    // “POSITIVE”, “NEGATIVE”, “ALL”
    @Column(name = "target_audience", length = 50)
    private String targetAudience;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public BusinessCallCampaignStrategySelection() {}

    // Lifecycle callbacks
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public CallCampaignStrategies getCallCampaignStrategy() {
        return callCampaignStrategy;
    }

    public void setCallCampaignStrategy(CallCampaignStrategies callCampaignStrategy) {
        this.callCampaignStrategy = callCampaignStrategy;
    }

    public String getCallCampaignVoice() {
        return callCampaignVoice;
    }

    public void setCallCampaignVoice(String callCampaignVoice) {
        this.callCampaignVoice = callCampaignVoice;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
