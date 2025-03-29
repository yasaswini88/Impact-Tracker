package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "map_suggested_features")
public class MapSuggestedFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "map_communication_id", nullable = false)
    private MapCommunication mapCommunication;

    @ElementCollection
    @CollectionTable(name = "suggested_features", joinColumns = @JoinColumn(name = "map_suggested_features_id"))
    @Column(name = "feature_name")
    private List<String> suggestedFeatures;

    @Column(name = "analysis_date", nullable = false)
    private LocalDateTime analysisDate;

    // Constructors
    public MapSuggestedFeatures() {
        this.analysisDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MapCommunication getMapCommunication() {
        return mapCommunication;
    }

    public void setMapCommunication(MapCommunication mapCommunication) {
        this.mapCommunication = mapCommunication;
    }

    public List<String> getSuggestedFeatures() {
        return suggestedFeatures;
    }

    public void setSuggestedFeatures(List<String> suggestedFeatures) {
        this.suggestedFeatures = suggestedFeatures;
    }

    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }
}
