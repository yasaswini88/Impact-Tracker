package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.GlobalSeasonalTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSeasonalTrendRepository extends JpaRepository<GlobalSeasonalTrend, Long> {
    GlobalSeasonalTrend findByBusinessType(String businessType);
}
