package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignStrategySelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface BusinessCallCampaignStrategySelectionRepository 
    extends JpaRepository<BusinessCallCampaignStrategySelection, Long> 
{
    // Optional: add custom queries if you need them


     @Query("SELECT s FROM BusinessCallCampaignStrategySelection s " +
           "WHERE s.business.businessId = :businessId " +
           "ORDER BY s.createdAt DESC")
    List<BusinessCallCampaignStrategySelection> findAllByBusinessIdDesc(@Param("businessId") Long businessId);

  
}
