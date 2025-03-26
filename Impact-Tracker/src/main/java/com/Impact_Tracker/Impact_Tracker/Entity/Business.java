package com.Impact_Tracker.Impact_Tracker.Entity;
import java.util.List;
import com.Impact_Tracker.Impact_Tracker.Entity.Feature;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessSuggestedFeature;


import jakarta.persistence.*;
import java.time.LocalTime;
import com.Impact_Tracker.Impact_Tracker.Entity.Plans;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessId;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String businessName;

    @Column(columnDefinition = "TIME")
    private LocalTime openingTime;

    @Column(columnDefinition = "TIME")
    private LocalTime closingTime;

    private String phoneNumber;
    private String email;
    private String zipCode;

     @Column(nullable = true)
    private Double longitude;

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = false)  
    private String password;

    @Column(nullable = true)  
private String businessType;

@Column(nullable = true) 
private String businessSize = "small";  





@Column(nullable = true)
    private String address;

    @Column(nullable = true,length=500)
    private String googlePlacesLink; 


//     @Column(nullable = true)
// private Integer ageOfAccount;


@Column(nullable = true)
private LocalDateTime createdDate = LocalDateTime.now();


@ManyToOne
@JoinColumn(name = "plan_id")
private Plans plan;

@Column(nullable = true)
private LocalDateTime dateOfPlanUpdated;

@ManyToMany
@JoinTable(
    name = "business_suggested_features",
    joinColumns = @JoinColumn(name = "business_id"),
    inverseJoinColumns = @JoinColumn(name = "feature_id")
)
@JsonManagedReference
private List<Feature> features;





    // Constructors
    public Business() {
    }

    // Getters and Setters
    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessSize() {
        return businessSize;
    }

    public void setBusinessSize(String businessSize) {
        this.businessSize = businessSize;
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


    // public Integer getAgeOfAccount() {
    //     return ageOfAccount;
    // }

    // public void setAgeOfAccount(Integer ageOfAccount) {
    //     this.ageOfAccount = ageOfAccount;
    // }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }




    public Plans getPlan() {
        return plan;
    }


    public void setPlan(Plans plan) {
        this.plan = plan;
    }

    public LocalDateTime getDateOfPlanUpdated() {
        return dateOfPlanUpdated;
    }

    public void setDateOfPlanUpdated(LocalDateTime dateOfPlanUpdated) {
        this.dateOfPlanUpdated = dateOfPlanUpdated;
    }


    public List<Feature> getFeatures() {
        return features;
    }


    public void setFeatures(List<Feature> features) {
        this.features = features;
    }









    
}
