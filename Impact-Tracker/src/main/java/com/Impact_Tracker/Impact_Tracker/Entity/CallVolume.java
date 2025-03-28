package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "call_volume")
public class CallVolume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long callVolumeId;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Using ElementCollection to store list of strings (month names)
    @ElementCollection
    @CollectionTable(name = "call_volume_months", joinColumns = @JoinColumn(name = "call_volume_id"))
    @Column(name = "month")
    private List<String> months;

    // Lists for answered, missed, voicemail counts
    @ElementCollection
    @CollectionTable(name = "call_volume_answered", joinColumns = @JoinColumn(name = "call_volume_id"))
    @Column(name = "answered")
    private List<Integer> answered;

    @ElementCollection
    @CollectionTable(name = "call_volume_missed", joinColumns = @JoinColumn(name = "call_volume_id"))
    @Column(name = "missed")
    private List<Integer> missed;

    @ElementCollection
    @CollectionTable(name = "call_volume_voicemail", joinColumns = @JoinColumn(name = "call_volume_id"))
    @Column(name = "voicemail")
    private List<Integer> voicemail;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @PrePersist
    public void onCreate() {
        this.dateCreated = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getCallVolumeId() {
        return callVolumeId;
    }

    public void setCallVolumeId(Long callVolumeId) {
        this.callVolumeId = callVolumeId;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<Integer> getAnswered() {
        return answered;
    }

    public void setAnswered(List<Integer> answered) {
        this.answered = answered;
    }

    public List<Integer> getMissed() {
        return missed;
    }

    public void setMissed(List<Integer> missed) {
        this.missed = missed;
    }

    public List<Integer> getVoicemail() {
        return voicemail;
    }

    public void setVoicemail(List<Integer> voicemail) {
        this.voicemail = voicemail;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
