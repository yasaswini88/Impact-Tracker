package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification;
import com.Impact_Tracker.Impact_Tracker.Entity.WeatherForecast;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessWeatherNotificationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.WeatherForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIWeatherService {

    @Autowired
    private WeatherForecastRepository weatherForecastRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessWeatherNotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Runs every 15 minutes or so to handle new forecasts that the "AI" has not processed yet.
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void checkAndHandleForecasts() {
        // 1) Get all forecasts where aiHandled = false
        List<WeatherForecast> forecasts = weatherForecastRepository.findAll();
        for (WeatherForecast forecast : forecasts) {
            if (forecast.getAiHandled() == null || !forecast.getAiHandled()) {
                // 2) Check condition
                if (shouldAlertBusiness(forecast)) {
                    // Send Email to the business associated
                    // For demonstration, we'll just pick the first business in the DB
                    // or you might have a real mapping from forecast -> business
                    var someBusiness = businessRepository.findAll().stream().findFirst();
                    if (someBusiness.isPresent()) {
                        Long businessId = someBusiness.get().getBusinessId();
                        String toEmail = someBusiness.get().getEmail(); // from your business entity

                        // Build the email body
                        String subject = "Weather Alert: " + forecast.getWeatherCondition();
                        String bodyHtml = buildEmailHtml(forecast, businessId);

                        // Send Email
                        emailService.sendEmailWithLinks(toEmail, subject, bodyHtml);

                        // Save a new notification row
                        BusinessWeatherNotification notification = new BusinessWeatherNotification();
                        notification.setBusinessId(businessId);
                        notification.setForecastId(forecast.getForecastId());
                        notification.setStatus("SENT");
                        notification.setBusinessConfirmed("PENDING");
                        notification.setCreatedAt(LocalDateTime.now());
                        notification.setUpdatedAt(LocalDateTime.now());
                        notificationRepository.save(notification);
                    }
                }

                // 3) Mark aiHandled = true so we don't process it next time
                forecast.setAiHandled(true);
                forecast.setUpdatedAt(LocalDateTime.now());
                weatherForecastRepository.save(forecast);
            }
        }
    }

    private boolean shouldAlertBusiness(WeatherForecast forecast) {
        // Example condition: weatherCondition in {broken clouds, overcast clouds} OR severity = High
        // And forecast.aiHandled = false
        String wc = forecast.getWeatherCondition();
        String sv = forecast.getSeverity();
        if (wc == null) wc = "";
        if (sv == null) sv = "";

        boolean condition1 = wc.equalsIgnoreCase("broken clouds");
        boolean condition2 = wc.equalsIgnoreCase("overcast clouds");
        boolean condition3 = sv.equalsIgnoreCase("High");

        return (condition1 || condition2 || condition3);
    }

    private String buildEmailHtml(WeatherForecast forecast, Long businessId) {
        // Here we craft the HTML with two links 
        // The forecastId and businessId are passed as query parameters
        return "<html><body>"
                + "<h3>Weather Alert!</h3>"
                + "<p>The weather condition in " + forecast.getCityName() 
                + " is " + forecast.getWeatherCondition() + "</p>"
                + "<p>Do you want me to handle the appointments for you?</p>"
                + "<p>"
                + "<a href='http://localhost:8080/api/v1/ai-weather/handle-yes?forecastId=" + forecast.getForecastId() 
                    + "&businessId=" + businessId + "'>YES, handle</a><br>"
                + "<a href='http://localhost:8080/api/v1/ai-weather/handle-no?forecastId=" + forecast.getForecastId() 
                    + "&businessId=" + businessId + "'>NO, ignore</a>"
                + "</p>"
                + "</body></html>";
    }
}
