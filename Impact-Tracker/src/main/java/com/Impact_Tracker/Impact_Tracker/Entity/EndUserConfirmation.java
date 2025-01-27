package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "end_user_confirmation")
public class EndUserConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endUserConfirmationId;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Long businessId;

    @Column(nullable = false)
    private Long customerId;

    // "PENDING", "YES", "NO", "RESCHEDULE", etc., depending on your use case
    @Column(nullable = false)
    private String customerResponse;  

    // "SENT", "RESPONDED", etc.
    @Column(nullable = false)
    private String status;            

    private LocalDateTime dateResponded;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public EndUserConfirmation() { }

    // Getters and setters
    public Long getEndUserConfirmationId() {
        return endUserConfirmationId;
    }

    public void setEndUserConfirmationId(Long endUserConfirmationId) {
        this.endUserConfirmationId = endUserConfirmationId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(String customerResponse) {
        this.customerResponse = customerResponse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateResponded() {
        return dateResponded;
    }

    public void setDateResponded(LocalDateTime dateResponded) {
        this.dateResponded = dateResponded;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
