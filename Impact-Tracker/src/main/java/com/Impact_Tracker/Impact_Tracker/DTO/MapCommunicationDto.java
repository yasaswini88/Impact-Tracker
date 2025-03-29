package com.Impact_Tracker.Impact_Tracker.DTO;

public class MapCommunicationDto {
    private Long id;
    private String name;
    private String address;
    private String googlePlacesLink;
    private String businessType;
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
