package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_google_review_confirmation")  // Or any table name you prefer
public class BusinessGoogleReviewConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For “Pending” / “Y” / “N”
    @Column(nullable = false, length = 10)
    private String userResponse;

    @OneToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // When the user finally responds
    @Column(name = "response_date", nullable = true)
    private LocalDateTime responseDate;

    public BusinessGoogleReviewConfirmation() {
    }

    // --- Getters and Setters ---
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
