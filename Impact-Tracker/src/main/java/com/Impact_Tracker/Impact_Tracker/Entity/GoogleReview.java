package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "google_review")
public class GoogleReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = true, length = 2000)
    private String reviewText;

    @Column(nullable = true)
    private Integer stars; 


    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "published_at", nullable = true)
    private LocalDateTime publishedAt; 

    // Constructors
    public GoogleReview() {}

    public GoogleReview(String reviewText, Integer stars, Business business, LocalDateTime publishedAt) {
        this.reviewText = reviewText;
        this.stars = stars;

        this.business = business;
        this.publishedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }



    public Business getBusiness() { return business; }
    public void setBusiness(Business business) { this.business = business; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }


}
