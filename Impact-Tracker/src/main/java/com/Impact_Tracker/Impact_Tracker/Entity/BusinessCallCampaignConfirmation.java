package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_call_campaign_confirmation")
public class BusinessCallCampaignConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Pending", "Y", or "N"
    @Column(nullable = false, length = 10)
    private String userResponse;

    @OneToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "response_date", nullable = true)
    private LocalDateTime responseDate;

    public BusinessCallCampaignConfirmation() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }
}
