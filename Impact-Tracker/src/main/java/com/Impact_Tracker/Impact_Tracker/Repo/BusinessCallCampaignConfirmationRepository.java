package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessCallCampaignConfirmationRepository
      extends JpaRepository<BusinessCallCampaignConfirmation, Long> {

    BusinessCallCampaignConfirmation findByBusiness_BusinessId(Long businessId);
}
