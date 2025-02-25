package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignOutgoingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface CallCampaignOutgoingLogRepository extends JpaRepository<CallCampaignOutgoingLog, Long> {
  

     
    @Query("SELECT c FROM CallCampaignOutgoingLog c " +
           "WHERE c.strategySelection.business.businessId = :bizId " +
           "ORDER BY c.calledAt DESC")
    List<CallCampaignOutgoingLog> findByBusinessIdDesc(@Param("bizId") Long bizId);
}
