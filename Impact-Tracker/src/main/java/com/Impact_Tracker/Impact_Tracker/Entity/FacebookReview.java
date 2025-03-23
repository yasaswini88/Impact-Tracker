package com.Impact_Tracker.Impact_Tracker.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "facebook_review")
public class FacebookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(length = 5000)
    private String reviewText;

    private LocalDateTime reviewDate;
    private Integer likesCount;
    private Integer commentsCount;

    private String reviewerName;
    private String reviewerProfilePicUrl;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Constructors
    public FacebookReview() {}

    public FacebookReview(String reviewText, LocalDateTime reviewDate, Integer likesCount, Integer commentsCount,
                          String reviewerName, String reviewerProfilePicUrl, Business business) {
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.reviewerName = reviewerName;
        this.reviewerProfilePicUrl = reviewerProfilePicUrl;
        this.business = business;
    }

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

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerProfilePicUrl() {
        return reviewerProfilePicUrl;
    }

    public void setReviewerProfilePicUrl(String reviewerProfilePicUrl) {
        this.reviewerProfilePicUrl = reviewerProfilePicUrl;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

   @Override
public String toString() {
    return "FacebookReview{" +
            "reviewId=" + reviewId +
            ", reviewText='" + reviewText + '\'' +
            ", reviewDate=" + reviewDate +
            ", likesCount=" + likesCount +
            ", commentsCount=" + commentsCount +
            ", reviewerName='" + reviewerName + '\'' +
            ", reviewerProfilePicUrl='" + reviewerProfilePicUrl + '\'' +
            ", business=" + (business != null ? business.getBusinessId() : null) +
            '}';
}

}
