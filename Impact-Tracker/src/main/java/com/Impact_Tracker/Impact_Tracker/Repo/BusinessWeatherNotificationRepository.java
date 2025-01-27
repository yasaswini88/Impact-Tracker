package com.Impact_Tracker.Impact_Tracker.Repo;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessWeatherNotificationRepository extends JpaRepository<BusinessWeatherNotification, Long> {
    // any custom query methods if needed
}
