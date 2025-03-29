package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "map_communication_review")
public class MapCommunicationReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = true, length = 2000)
    private String reviewText;

    @Column(nullable = true)
    private Integer stars;

    // Associate the review with a MapCommunication entity
    @ManyToOne
    @JoinColumn(name = "map_communication_id", nullable = false)
    private MapCommunication mapCommunication;

    @Column(name = "published_at", nullable = true)
    private LocalDateTime publishedAt;

    // Constructors
    public MapCommunicationReview() {}

    // Getters and Setters
    public Long getReviewId() {
        return reviewId;
    }
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public Integer getStars() {
        return stars;
    }
    public void setStars(Integer stars) {
        this.stars = stars;
    }
    public MapCommunication getMapCommunication() {
        return mapCommunication;
    }
    public void setMapCommunication(MapCommunication mapCommunication) {
        this.mapCommunication = mapCommunication;
    }
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
