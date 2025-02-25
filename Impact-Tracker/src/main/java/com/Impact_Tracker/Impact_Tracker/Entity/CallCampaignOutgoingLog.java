package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_campaign_outgoing_log")
public class CallCampaignOutgoingLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the strategy_selection row that triggered the call
    @ManyToOne
    @JoinColumn(name = "strategy_selection_id", nullable = false)
    private BusinessCallCampaignStrategySelection strategySelection;

    // The phone number called
    @Column(name = "client_phone_number", nullable = false, length = 50)
    private String clientPhoneNumber;

    // The time the call was triggered
    @Column(name = "called_at", nullable = false)
    private LocalDateTime calledAt;

    // Optionally store Twilio’s response or call SID if you want
    @Column(name = "twilio_response", length = 1000)
    private String twilioResponse;

    // Auto‐set the timestamp right before inserting the row
    @PrePersist
    public void onCreate() {
        this.calledAt = LocalDateTime.now();
    }

    // Constructors, getters, and setters

    public CallCampaignOutgoingLog() {}

    public Long getId() {
        return id;
    }

    public BusinessCallCampaignStrategySelection getStrategySelection() {
        return strategySelection;
    }

    public void setStrategySelection(BusinessCallCampaignStrategySelection strategySelection) {
        this.strategySelection = strategySelection;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }

    public String getTwilioResponse() {
        return twilioResponse;
    }

    public void setTwilioResponse(String twilioResponse) {
        this.twilioResponse = twilioResponse;
    }
}
