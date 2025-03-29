package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "map_communication_review_insights")
public class MapCommunicationReviewInsights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Overall summary from the AI analysis
    @Column(columnDefinition = "TEXT", nullable = true)
    private String insights;

    // Fields for positive and negative points
    @Column(columnDefinition = "TEXT")
    private String positivePoints;

    @Column(columnDefinition = "TEXT")
    private String negativePoints;

    // Associate the insights with a MapCommunication entity
    @OneToOne
    @JoinColumn(name = "map_communication_id", nullable = false)
    private MapCommunication mapCommunication;

    @Column(name = "analysis_date", nullable = true)
    private LocalDateTime analysisDate;

    // Constructors
    public MapCommunicationReviewInsights() {}

    // Getters and Setters
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
    public MapCommunication getMapCommunication() {
        return mapCommunication;
    }
    public void setMapCommunication(MapCommunication mapCommunication) {
        this.mapCommunication = mapCommunication;
    }
    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }
    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
}
