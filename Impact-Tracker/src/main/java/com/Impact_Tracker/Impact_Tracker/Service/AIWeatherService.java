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

    @Autowired
    private EmailService emailService;  // Email functionality

    @Autowired
    private TwilioStudioService twilioStudioService;  // Twilio functionality

    /**
     * Runs every 3 minutes to handle new forecasts that the "AI" has not processed yet.
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    // @Scheduled(cron="0 1 1 * * *")
    public void checkAndHandleForecasts() {
        // 1) Get all forecasts
        List<WeatherForecast> forecasts = weatherForecastRepository.findAll();

        for (WeatherForecast forecast : forecasts) {
            // We treat "true" as AI-handled; everything else means not handled yet
            if (forecast.getAiHandled() == null || !"true".equalsIgnoreCase(forecast.getAiHandled())) {

                // 2) Decide whether we should alert the business
                if (shouldAlertBusiness(forecast)) {
                    // For demonstration, pick the first business in the DB
                  
var someBusiness = businessRepository.findAll().stream()
    .filter(b -> b.getBusinessType() != null)
    .filter(b -> b.getBusinessType().equalsIgnoreCase("Lawn Mowing"))
    .findFirst();


                    if (someBusiness.isPresent()) {
                        Long businessId = someBusiness.get().getBusinessId();
                        Long fcId = forecast.getForecastId();

                        // =========================
                        // 1) Send an EMAIL
                        // =========================
                        String toEmail = someBusiness.get().getEmail(); // from your business entity
                        String subject = "Weather Alert: " + forecast.getWeatherCondition();
                        String bodyHtml = buildEmailHtml(forecast, businessId);

                        emailService.sendEmailWithLinks(toEmail, subject, bodyHtml);

                        // =========================
                        // 2) Make a TWILIO PHONE CALL
                        // =========================
                        String phone = someBusiness.get().getPhoneNumber();
                        String textForFlow = "Weather Alert! Press 1 for yes, Press 2 for no.";

                        String callResult = twilioStudioService.createStudioExecution(
                                phone,
                                textForFlow,
                                fcId,
                                businessId
                        );
                        System.out.println("Twilio call result: " + callResult);

                        // =========================
                        // 3) Save a NOTIFICATION record
                        // =========================
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

                    else {
    // If no "Lawn Mowing" business found, skip or mark forecast as handled
    forecast.setAiHandled("true");
    forecast.setUpdatedAt(LocalDateTime.now());
    weatherForecastRepository.save(forecast);
}

                }

                // Even if shouldAlertBusiness(...) is false OR no business found,
                // mark the forecast as handled to avoid repeated checks
                forecast.setAiHandled("true");
                forecast.setUpdatedAt(LocalDateTime.now());
                weatherForecastRepository.save(forecast);
            }
        }
    }

    /**
     * Decide whether we should alert the business about the forecast.
     * Example condition: weatherCondition in {broken clouds, overcast clouds, scattered clouds, clear sky}
     * OR severity = "High".
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
       
        boolean condition6 = wc.equalsIgnoreCase("few clouds");

        return (condition1 || condition2 || condition3 || condition4 || condition5 || condition6);
    }

    /**
     * Build HTML body for the email (now used since we re-enabled emails).
     */
    private String buildEmailHtml(WeatherForecast forecast, Long businessId) {
        return "<html><body>"
                + "<h3>Weather Alert!</h3>"
                + "<p>The weather condition in " + forecast.getZipCode()
                + " is " + forecast.getWeatherCondition() + "</p>"
                + "<p>Do you want me to handle the appointments for you?</p>"
                + "<p>"
                + "<a href='http://52.3.145.159:8080/api/v1/ai-weather/handle-yes?forecastId="
                    + forecast.getForecastId() + "&businessId=" + businessId
                    + "'>YES, handle</a><br>"
                + "<a href='http://52.3.145.159:8080/api/v1/ai-weather/handle-no?forecastId="
                    + forecast.getForecastId() + "&businessId=" + businessId
                    + "'>NO, ignore</a>"
                + "</p>"
                + "</body></html>";
    }

}
