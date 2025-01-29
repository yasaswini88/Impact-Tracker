package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification;
import com.Impact_Tracker.Impact_Tracker.Entity.WeatherForecast;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessWeatherNotificationRepository;
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

    // We keep the EmailService, but will comment out its usage:
    @Autowired
    private EmailService emailService;

    @Autowired
    private TwilioStudioService twilioStudioService;

    /**
     * Runs every 15 minutes or so to handle new forecasts that the "AI" has not processed yet.
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    // @Scheduled(cron="0 1 1 * * *")
    // @Scheduled(cron="0 1 1 * * *")
    public void checkAndHandleForecasts() {
        // 1) Get all forecasts
        List<WeatherForecast> forecasts = weatherForecastRepository.findAll();
        for (WeatherForecast forecast : forecasts) {

            // We treat "true" as AI-handled; everything else is not handled.
            if (forecast.getAiHandled() == null || !"true".equalsIgnoreCase(forecast.getAiHandled())) {

                // 2) Check condition
                if (shouldAlertBusiness(forecast)) {
                    // For demonstration, we just pick the first business in the DB
                    var someBusiness = businessRepository.findAll().stream().findFirst();

                    /*
                    // =========================
                    // COMMENTED-OUT EMAIL CODE
                    // =========================
                    if (someBusiness.isPresent()) {
                        Long businessId = someBusiness.get().getBusinessId();
                        String toEmail  = someBusiness.get().getEmail(); // from your business entity

                        // Build the email body
                        String subject  = "Weather Alert: " + forecast.getWeatherCondition();
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

                        // Mark forecast.aiHandled='true'
                        forecast.setAiHandled("true");
                        forecast.setUpdatedAt(LocalDateTime.now());
                        weatherForecastRepository.save(forecast);
                    }
                    */

                    // =========================
                    // ACTIVE PHONE-CALL CODE
                    // =========================
                    if (someBusiness.isPresent()) {
                        Long businessId = someBusiness.get().getBusinessId();
                        Long fcId       = forecast.getForecastId();

                        // 1) Grab the phone number
                        String phone = someBusiness.get().getPhoneNumber();

                        // 2) Make the Twilio call
                        String textForFlow = "Weather Alert! Press 1 for yes, Press 2 for no.";
                        String callResult = twilioStudioService.createStudioExecution(
                                phone,
                                textForFlow,
                                fcId,
                                businessId
                                // If you want to specify flowSid, add it as needed
                                // e.g. , "FWa0b4b625f359513cd29d6e5f38e9345e"
                        );
                        System.out.println("Twilio call result: " + callResult);

                        // 3) Save the notification
                        BusinessWeatherNotification notification = new BusinessWeatherNotification();
                        notification.setBusinessId(businessId);
                        notification.setForecastId(forecast.getForecastId());
                        notification.setStatus("SENT");
                        notification.setBusinessConfirmed("PENDING");
                        notification.setCreatedAt(LocalDateTime.now());
                        notification.setUpdatedAt(LocalDateTime.now());
                        notificationRepository.save(notification);

                        // 4) Mark forecast.aiHandled = 'true'
                        forecast.setAiHandled("true");
                        forecast.setUpdatedAt(LocalDateTime.now());
                        weatherForecastRepository.save(forecast);
                    }
                }

                // Even if shouldAlertBusiness(...) is false, or someBusiness is not present,
                // we still mark the forecast as handled to avoid repeated checks:
                forecast.setAiHandled("true");
                forecast.setUpdatedAt(LocalDateTime.now());
                weatherForecastRepository.save(forecast);
            }
        }
    }

    /**
     * Decide whether we should alert the business about the forecast.
     * Example condition: weatherCondition in {broken clouds, overcast clouds} OR severity = "High".
     */
    private boolean shouldAlertBusiness(WeatherForecast forecast) {
        String wc = forecast.getWeatherCondition();
        String sv = forecast.getSeverity();
        if (wc == null) wc = "";
        if (sv == null) sv = "";

        boolean condition1 = wc.equalsIgnoreCase("broken clouds");

        boolean condition2 = wc.equalsIgnoreCase("overcast clouds");
        boolean condition3 = sv.equalsIgnoreCase("High");
        boolean condition4 = wc.equalsIgnoreCase("clear sky");
        boolean condition5 = wc.equalsIgnoreCase("scattered clouds");
        return (condition1 || condition2 || condition3 || condition4 || condition5);
    }

    /**
     * Build HTML body for the email (currently not used, but retained in case you re-enable email).
     */
    private String buildEmailHtml(WeatherForecast forecast, Long businessId) {
        return "<html><body>"
                + "<h3>Weather Alert!</h3>"
                + "<p>The weather condition in " + forecast.getZipCode()
                + " is " + forecast.getWeatherCondition() + "</p>"
                + "<p>Do you want me to handle the appointments for you?</p>"
                + "<p>"
                + "<a href='http://52.3.145.159:8080/api/v1/ai-weather/handle-yes?forecastId=" + forecast.getForecastId()
                    + "&businessId=" + businessId + "'>YES, handle</a><br>"
                + "<a href='http://52.3.145.159:8080/api/v1/ai-weather/handle-no?forecastId=" + forecast.getForecastId()
                    + "&businessId=" + businessId + "'>NO, ignore</a>"
                + "</p>"
                + "</body></html>";
    }

}
