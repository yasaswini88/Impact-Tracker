package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.AggregatedCallVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedCallVolumeRepository extends JpaRepository<AggregatedCallVolume, Long> {
    // You can add custom queries later if needed.
}
