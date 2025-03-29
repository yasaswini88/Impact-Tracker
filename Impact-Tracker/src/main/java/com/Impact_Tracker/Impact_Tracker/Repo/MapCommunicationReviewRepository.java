package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapCommunicationReviewRepository extends JpaRepository<MapCommunicationReview, Long> {
    // Add custom queries if needed
}
