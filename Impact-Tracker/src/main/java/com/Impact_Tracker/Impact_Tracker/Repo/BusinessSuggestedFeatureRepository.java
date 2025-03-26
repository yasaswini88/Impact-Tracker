package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessSuggestedFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusinessSuggestedFeatureRepository extends JpaRepository<BusinessSuggestedFeature, Long> {
    List<BusinessSuggestedFeature> findByBusiness_BusinessId(Long businessId);
}
