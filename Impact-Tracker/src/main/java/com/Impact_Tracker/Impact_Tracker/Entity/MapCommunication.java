package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "map_communication")
public class MapCommunication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;

    @Column(nullable = true, length = 500)
    private String googlePlacesLink;

    @Column(nullable = true)
    private String businessType;

    // Constructors
    public MapCommunication() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getGooglePlacesLink() {
        return googlePlacesLink;
    }
    public void setGooglePlacesLink(String googlePlacesLink) {
        this.googlePlacesLink = googlePlacesLink;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
