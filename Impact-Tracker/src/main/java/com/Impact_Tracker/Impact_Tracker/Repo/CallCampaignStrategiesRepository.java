package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignStrategies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallCampaignStrategiesRepository extends JpaRepository<CallCampaignStrategies, Long> {
    // You can add custom queries if needed
}
