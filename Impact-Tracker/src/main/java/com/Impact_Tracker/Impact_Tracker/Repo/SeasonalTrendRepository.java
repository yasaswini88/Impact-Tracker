package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.SeasonalTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonalTrendRepository extends JpaRepository<SeasonalTrend, Long> {
    SeasonalTrend findTopByBusiness_BusinessIdOrderByGeneratedAtDesc(Long businessId);
}
