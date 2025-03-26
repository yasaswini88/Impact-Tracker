package com.Impact_Tracker.Impact_Tracker.Entity;
import java.util.List;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @Column(name = "feature_name", nullable = false, length = 200)
    private String featureName;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @ManyToMany(mappedBy = "features")
    @JsonBackReference

private List<Business> businesses;


    public Feature() {
        this.createdTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }


    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }



}
