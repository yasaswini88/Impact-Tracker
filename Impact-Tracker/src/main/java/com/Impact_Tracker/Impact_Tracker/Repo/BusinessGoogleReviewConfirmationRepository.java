package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessGoogleReviewConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessGoogleReviewConfirmationRepository 
       extends JpaRepository<BusinessGoogleReviewConfirmation, Long> {

    // Optional: find by business ID
    BusinessGoogleReviewConfirmation findByBusiness_BusinessId(Long businessId);
}
