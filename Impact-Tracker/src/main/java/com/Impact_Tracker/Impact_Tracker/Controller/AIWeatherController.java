package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessWeatherNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ai-weather")
public class AIWeatherController {

    @Autowired
    private BusinessWeatherNotificationRepository notificationRepository;

    @GetMapping("/handle-yes")
    public String handleYes(@RequestParam("forecastId") Long forecastId,
                            @RequestParam("businessId") Long businessId) {
        // Find the corresponding BusinessWeatherNotification
        Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findAll().stream()
                .filter(n -> n.getForecastId() != null && n.getForecastId().equals(forecastId))
                .filter(n -> n.getBusinessId().equals(businessId))
                .filter(n -> n.getBusinessConfirmed().equals("PENDING"))
                .findFirst();

        if (notificationOpt.isEmpty()) {
            return "Notification not found or already responded.";
        }

        // Update the notification
        BusinessWeatherNotification notification = notificationOpt.get();
        notification.setBusinessConfirmed("Y");
        notification.setStatus("RESPONDED");
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // You could trigger additional logic here, e.g. rescheduling appointments, etc.
        return "You have chosen YES. We will handle your appointments accordingly.";
    }

    @GetMapping("/handle-no")
    public String handleNo(@RequestParam("forecastId") Long forecastId,
                           @RequestParam("businessId") Long businessId) {
        Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findAll().stream()
                .filter(n -> n.getForecastId() != null && n.getForecastId().equals(forecastId))
                .filter(n -> n.getBusinessId().equals(businessId))
                .filter(n -> n.getBusinessConfirmed().equals("PENDING"))
                .findFirst();

        if (notificationOpt.isEmpty()) {
            return "Notification not found or already responded.";
        }

        // Update the notification
        BusinessWeatherNotification notification = notificationOpt.get();
        notification.setBusinessConfirmed("N");
        notification.setStatus("RESPONDED");
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        return "You have chosen NO. We will not handle your appointments.";
    }
}
