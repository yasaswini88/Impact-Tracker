package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.CallVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallVolumeRepository extends JpaRepository<CallVolume, Long> {
    // Find call volume records by business id
    List<CallVolume> findByBusiness_BusinessId(Long businessId);
    List<CallVolume> findAll();
}
