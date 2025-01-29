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

    // @GetMapping("/handle-yes")
    // public String handleYes(@RequestParam("forecastId") Long forecastId,
    //                         @RequestParam("businessId") Long businessId) {
    //     // Find the corresponding BusinessWeatherNotification
    //     Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findAll().stream()
    //             .filter(n -> n.getForecastId() != null && n.getForecastId().equals(forecastId))
    //             .filter(n -> n.getBusinessId().equals(businessId))
    //             .filter(n -> n.getBusinessConfirmed().equals("PENDING"))
    //             .findFirst();

    //     if (notificationOpt.isEmpty()) {
    //         return "Notification not found or already responded.";
    //     }

    //     // Update the notification
    //     BusinessWeatherNotification notification = notificationOpt.get();
    //     notification.setBusinessConfirmed("Y");
    //     notification.setStatus("RESPONDED");
    //     notification.setUpdatedAt(LocalDateTime.now());
    //     notificationRepository.save(notification);

    //     // You could trigger additional logic here, e.g. rescheduling appointments, etc.
    //     return "You have chosen YES. We will handle your appointments accordingly.";
    // }

    // @GetMapping("/handle-no")
    // public String handleNo(@RequestParam("forecastId") Long forecastId,
    //                        @RequestParam("businessId") Long businessId) {
    //     Optional<BusinessWeatherNotification> notificationOpt = notificationRepository.findAll().stream()
    //             .filter(n -> n.getForecastId() != null && n.getForecastId().equals(forecastId))
    //             .filter(n -> n.getBusinessId().equals(businessId))
    //             .filter(n -> n.getBusinessConfirmed().equals("PENDING"))
    //             .findFirst();

    //     if (notificationOpt.isEmpty()) {
    //         return "Notification not found or already responded.";
    //     }

    //     // Update the notification
    //     BusinessWeatherNotification notification = notificationOpt.get();
    //     notification.setBusinessConfirmed("N");
    //     notification.setStatus("RESPONDED");
    //     notification.setUpdatedAt(LocalDateTime.now());
    //     notificationRepository.save(notification);

    //     return "You have chosen NO. We will not handle your appointments.";
    // }


    @GetMapping("/handle")
public String handleAny(
    @RequestParam("forecastId") Long forecastId,
    @RequestParam("businessId") Long businessId,
    @RequestParam("digit") String digit
) {
    // 1) Find the record
    Optional<BusinessWeatherNotification> notifOpt = notificationRepository.findAll().stream()
        .filter(n -> n.getForecastId() != null && n.getForecastId().equals(forecastId))
        .filter(n -> n.getBusinessId().equals(businessId))
        .filter(n -> "PENDING".equals(n.getBusinessConfirmed()))
        .findFirst();

    if (notifOpt.isEmpty()) {
        return "Notification not found or already responded.";
    }

    BusinessWeatherNotification notif = notifOpt.get();

    // 2) Decide based on digit
    if ("1".equals(digit)) {
        notif.setBusinessConfirmed("Y");
        notif.setStatus("RESPONDED");
        // Possibly do extra logic for "Yes" ...
    } else if ("2".equals(digit)) {
        notif.setBusinessConfirmed("N");
        notif.setStatus("RESPONDED");
        // Possibly do extra logic for "No" ...
    } else {
        return "Invalid digit pressed.";
    }

    notif.setUpdatedAt(LocalDateTime.now());
    notificationRepository.save(notif);

    // 3) Return a message
    if ("1".equals(digit)) {
        return "You pressed 1 (YES). We will handle your appointments accordingly.";
    } else {
        return "You pressed 2 (NO). We will not handle your appointments.";
    }
}


}
