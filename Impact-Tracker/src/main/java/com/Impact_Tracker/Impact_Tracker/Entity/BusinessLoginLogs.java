package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_login_logs")
public class BusinessLoginLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    // Foreign key to Business
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Record login time
    @Column(nullable = false)
    private LocalDateTime loginTime;

    // (Optional) Additional fields you might consider:
    // private String ipAddress;
    // private String userAgent;

    // Constructors
    public BusinessLoginLogs() {}

    public BusinessLoginLogs(Business business, LocalDateTime loginTime) {
        this.business = business;
        this.loginTime = loginTime;
    }

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
