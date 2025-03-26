package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "business_suggested_features")
public class BusinessSuggestedFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    // Constructors
    public BusinessSuggestedFeature() {}

    public BusinessSuggestedFeature(Business business, Feature feature) {
        this.business = business;
        this.feature = feature;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public Business getBusiness() { return business; }

    public void setBusiness(Business business) { this.business = business; }

    public Feature getFeature() { return feature; }

    public void setFeature(Feature feature) { this.feature = feature; }
}
