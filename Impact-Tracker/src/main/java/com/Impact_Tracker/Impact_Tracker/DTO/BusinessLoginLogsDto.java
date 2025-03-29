package com.Impact_Tracker.Impact_Tracker.DTO;

import java.time.LocalDateTime;

public class BusinessLoginLogsDto {
    private Long logId;
    private Long businessId;
    private LocalDateTime loginTime;

    // (Optional) additional fields e.g., ipAddress

    // Constructors
    public BusinessLoginLogsDto() {}

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
