
package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessWeatherNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/hotel")
public class HotelConfirmationController {

    @Autowired
    private BusinessWeatherNotificationRepository notificationRepository;

    @GetMapping("/handle-yes")
    public String handleYes(@RequestParam("notificationId") Long notificationId,
                            @RequestParam("businessId") Long businessId) {

        // 1) Find the corresponding record
        Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findById(notificationId);

        if (notificationOpt.isEmpty()) {
            return "Notification not found or already responded.";
        }

        BusinessWeatherNotification notif = notificationOpt.get();

        // 2) Check if it matches the correct business and is still PENDING
        if (!notif.getBusinessId().equals(businessId)) {
            return "Mismatched businessId!";
        }
        if (!"PENDING".equals(notif.getBusinessConfirmed())) {
            return "Notification already responded.";
        }

        // 3) Update to "Y"
        notif.setBusinessConfirmed("Y");
        notif.setStatus("RESPONDED");
        notif.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notif);

        return "Thank you. We will fetch your Google Reviews soon.";
    }

    @GetMapping("/handle-no")
    public String handleNo(@RequestParam("notificationId") Long notificationId,
                           @RequestParam("businessId") Long businessId) {

        // Same logic, set businessConfirmed="N"
        Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isEmpty()) {
            return "Notification not found or already responded.";
        }
        BusinessWeatherNotification notif = notificationOpt.get();

        if (!notif.getBusinessId().equals(businessId)) {
            return "Mismatched businessId!";
        }
        if (!"PENDING".equals(notif.getBusinessConfirmed())) {
            return "Notification already responded.";
        }

        notif.setBusinessConfirmed("N");
        notif.setStatus("RESPONDED");
        notif.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notif);

        return "Understood. We will not fetch additional reviews.";
    }
}
