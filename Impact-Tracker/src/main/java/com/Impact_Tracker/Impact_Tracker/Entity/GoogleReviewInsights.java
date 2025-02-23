package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "google_review_insights")
public class GoogleReviewInsights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store overall summary (required)
    @Column(columnDefinition = "TEXT", nullable = true)
    private String insights;  

    // New fields for positive and negative points
    @Column(columnDefinition = "TEXT")
    private String positivePoints;  

    @Column(columnDefinition = "TEXT")
    private String negativePoints;

    @OneToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

     @Column(name = "analysis_date", nullable = true)
    private LocalDateTime analysisDate;

    

    // Constructors
    public GoogleReviewInsights() {}

    // Getters and Setters ...
    public Long getId() {
        return id;
    }
    public String getInsights() {
        return insights;
    }
    public void setInsights(String insights) {
        this.insights = insights;
    }

    public String getPositivePoints() {
        return positivePoints;
    }
    public void setPositivePoints(String positivePoints) {
        this.positivePoints = positivePoints;
    }

    public String getNegativePoints() {
        return negativePoints;
    }
    public void setNegativePoints(String negativePoints) {
        this.negativePoints = negativePoints;
    }

    public Business getBusiness() {
        return business;
    }
    public void setBusiness(Business business) {
        this.business = business;
    }

    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }


    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
    
}
