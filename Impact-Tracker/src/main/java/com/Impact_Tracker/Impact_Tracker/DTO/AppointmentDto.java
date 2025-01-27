package com.Impact_Tracker.Impact_Tracker.DTO;

public class AppointmentDto {

    private Long appointmentId;
    private String appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
    private String appointmentType;
    private String appointmentStatus;

    private Long customerId;    // for referencing Customer
    private Long businessId;    // for referencing Business

    private String appointmentRescheduled;

    private String customerName;

   

    // Constructors
    public AppointmentDto() {
    }

   
    public AppointmentDto(Long appointmentId, String appointmentDate, String appointmentStartTime,
                          String appointmentEndTime, String appointmentType, String appointmentStatus,
                          Long customerId, Long businessId, String appointmentRescheduled) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentType = appointmentType;
        this.appointmentStatus = appointmentStatus;
        this.customerId = customerId;
        this.businessId = businessId;
        this.appointmentRescheduled = appointmentRescheduled;
    }

    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentStartTime() {
        return appointmentStartTime;
    }
    public void setAppointmentStartTime(String appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public String getAppointmentEndTime() {
        return appointmentEndTime;
    }
    public void setAppointmentEndTime(String appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }
    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBusinessId() {
        return businessId;
    }
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

     public String getAppointmentRescheduled() {
        return appointmentRescheduled;
    }

    public void setAppointmentRescheduled(String appointmentRescheduled) {
        this.appointmentRescheduled = appointmentRescheduled;
    }

     // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
