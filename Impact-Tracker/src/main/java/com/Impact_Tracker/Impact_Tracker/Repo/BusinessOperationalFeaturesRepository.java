package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessOperationalFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessOperationalFeaturesRepository extends JpaRepository<BusinessOperationalFeatures, Long> {
    
}
   
