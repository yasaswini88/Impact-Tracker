package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "business_operational_features")
public class BusinessOperationalFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to Business
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Foreign key to Feature
    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    // Date when the feature was enabled for the business
    @Column(name = "feature_enabled", nullable = false)
    private LocalDate featureEnabled;

    // Constructors
    public BusinessOperationalFeatures() {}

    public BusinessOperationalFeatures(Business business, Feature feature, LocalDate featureEnabled) {
        this.business = business;
        this.feature = feature;
        this.featureEnabled = featureEnabled;
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

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public LocalDate getFeatureEnabled() {
        return featureEnabled;
    }

    public void setFeatureEnabled(LocalDate featureEnabled) {
        this.featureEnabled = featureEnabled;
    }
}
