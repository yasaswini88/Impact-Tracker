package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignStrategySelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessCallCampaignStrategySelectionRepository 
    extends JpaRepository<BusinessCallCampaignStrategySelection, Long> 
{
    // Optional: add custom queries if you need them

  
}
