package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReviewInsights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapCommunicationReviewInsightsRepository extends JpaRepository<MapCommunicationReviewInsights, Long> {
    // Additional custom queries if required
}
