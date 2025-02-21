package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessWeatherNotification; 
import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessWeatherNotificationRepository; 
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

/**
 * This service checks for negative sentiments in "hotel" businesses and sends alerts if negative > 30%.
 */
@Service
public class HotelNotificationService {

    @Autowired
    private SentimentAnalysisRepository sentimentAnalysisRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessWeatherNotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;        
    @Autowired
    private TwilioStudioService twilioStudioService;  

    /**
     * Scheduled method to check negative reviews for hotels.
     * Runs daily at 1:01 AM.
     * CRON format: second, minute, hour, dayOfMonth, month, dayOfWeek
     */
    // @Scheduled(cron = "0 1 1 * * *")       // Original daily schedule
    @Scheduled(cron = "0 0/2 * * * ?")  // Run every 2 minutes for testing
    public void checkHotelNegativeReviews() {
        System.out.println("=== HotelNotificationService: Starting checkHotelNegativeReviews() ===");

        // 1) Fetch all sentiments from DB
        List<SentimentAnalysis> allSentiments = sentimentAnalysisRepository.findAll();
        System.out.println("Fetched total SentimentAnalysis records: " + allSentiments.size());

        for (SentimentAnalysis sa : allSentiments) {
            // If aiHandled != "true", then we process it
            if (sa.getAiHandled() == null || !sa.getAiHandled()) {
                System.out.println("Found UNHANDLED sentiment ID: " + sa.getId()
                        + " for business ID: " + sa.getBusiness().getBusinessId());

                // 2) Check if the associated business is a hotel
                Business biz = sa.getBusiness();
                if (biz != null && "hotels".equalsIgnoreCase(biz.getBusinessType())) {
                    System.out.println("Business " + biz.getBusinessName() + " is a HOTEL. Checking negative ratio...");

                    // 3) Decide if negative ratio is above threshold
                    double negativeRatio = calculateNegativeRatioForBusiness(biz.getBusinessId());
                    System.out.println("Calculated negative ratio for business " + biz.getBusinessName()
                            + " is: " + negativeRatio + "%");

                    if (negativeRatio > 30.0) {
                        System.out.println("Negative ratio > 30%. Sending alert via email & Twilio...");

                        // ALERT the business
                        // -- Email
                             BusinessWeatherNotification notification = new BusinessWeatherNotification();
                        notification.setBusinessId(biz.getBusinessId());
                        notification.setForecastId(0L); // No "forecast" in this scenario
                        notification.setStatus("SENT");
                        notification.setBusinessConfirmed("PENDING");
                        notification.setCreatedAt(LocalDateTime.now());
                        notification.setUpdatedAt(LocalDateTime.now());
                        notificationRepository.save(notification);
                        System.out.println("Saved BusinessWeatherNotification record with status=SENT for businessId: "
                                           + biz.getBusinessId());

                        // Mark *all* unhandled sentiments for this biz as handled
                        markAllUnhandledAsHandled(biz.getBusinessId());

                        String toEmail = biz.getEmail();
                        String subject = "Negative Review Alert for Hotel!";
String emailBody = buildEmailHtml(negativeRatio, biz.getBusinessName(),
                                  notification.getNotificationId(),
                                  biz.getBusinessId());

                        System.out.println("Email To: " + toEmail);
                        System.out.println("Email Subject: " + subject);
                        System.out.println("Email Body:\n" + emailBody);

                        try {
                            emailService.sendEmailWithLinks(toEmail, subject, emailBody);
                            System.out.println("EmailService: email sent successfully to " + toEmail);
                        } catch (Exception e) {
                            System.out.println("EmailService: ERROR sending email => " + e.getMessage());
                            e.printStackTrace();
                        }

                        // -- Twilio call
                        // String phone = biz.getPhoneNumber();
                        // String textForFlow = String.format(
                        //     "Your hotel %s has %.2f%% negative reviews! Press 1 to acknowledge, Press 2 to ignore.",
                        //     biz.getBusinessName(),
                        //     negativeRatio
                        // );
                        // System.out.println("Making Twilio call to phone: " + phone);
                        // System.out.println("Twilio flow text: " + textForFlow);

                        // try {
                        //     String callResult = twilioStudioService.createStudioExecution(
                        //             phone,
                        //             textForFlow,
                        //             sa.getId(),     
                        //             biz.getBusinessId()
                        //     );
                        //     System.out.println("Twilio call result: " + callResult);
                        // } catch (Exception e) {
                        //     System.out.println("TwilioStudioService: ERROR placing call => " + e.getMessage());
                        //     e.printStackTrace();
                        // }

                        // -- Save a NOTIFICATION record
                       
                    } else {
                        System.out.println("Negative ratio <= 30%. Not sending alert. Marking this sentiment handled.");
                        sa.setAiHandled(true);
                        sa.setUpdatedAt(LocalDateTime.now());
                        sentimentAnalysisRepository.save(sa);
                    }
                } else {
                    // If not a hotel, or no business found, mark handled so we don't re-check
                    System.out.println("Business is not hotel or business is null. Marking sentiment handled. ID: " + sa.getId());
                    sa.setAiHandled(true);
                    sa.setUpdatedAt(LocalDateTime.now());
                    sentimentAnalysisRepository.save(sa);
                }
            }
        }

        System.out.println("=== HotelNotificationService: Finished checkHotelNegativeReviews() ===");
    }

    /**
     * Calculates how many negative sentiments exist (aiHandled=false) for the given business,
     * then returns the percentage that are negative.
     */
    private double calculateNegativeRatioForBusiness(Long businessId) {
        List<SentimentAnalysis> unhandledForBiz = sentimentAnalysisRepository.findAll()
                .stream()
                .filter(sa -> sa.getBusiness().getBusinessId().equals(businessId))
                .filter(sa -> sa.getAiHandled() == null || !sa.getAiHandled())
                .toList();

        if (unhandledForBiz.isEmpty()) {
            return 0.0;
        }

        long negativeCount = unhandledForBiz.stream()
                .filter(sa -> "Negative".equalsIgnoreCase(sa.getSentiment()))
                .count();

        return ((double) negativeCount / unhandledForBiz.size()) * 100.0;
    }

    /**
     * Marks all unhandled sentiments as handled for a given business.
     */
    private void markAllUnhandledAsHandled(Long businessId) {
        System.out.println("Marking all unhandled sentiments as handled for businessId: " + businessId);

        List<SentimentAnalysis> unhandledForBiz = sentimentAnalysisRepository.findAll()
                .stream()
                .filter(sa -> sa.getBusiness().getBusinessId().equals(businessId))
                .filter(sa -> sa.getAiHandled() == null || !sa.getAiHandled())
                .toList();

        for (SentimentAnalysis sa : unhandledForBiz) {
            sa.setAiHandled(true);
            sa.setUpdatedAt(LocalDateTime.now());
            sentimentAnalysisRepository.save(sa);
            System.out.println("  -> Marked sentiment ID " + sa.getId() + " as handled.");
        }
    }

    /**
     * Builds a simple HTML email body. 
     */

    private String buildEmailHtml(double negativeRatio, String hotelName, Long notificationId, Long businessId) {
    // Build the endpoint just like your weather links, but for a new path (or reuse existing):
    // e.g. /api/v1/hotel/handle-yes or something like /api/v1/ai-weather/handle-yes
    // as long as you have a corresponding GET mapping in your controller.
    String yesLink = "http://52.3.145.159:8080/api/v1/hotel/handle-yes?notificationId="
                     + notificationId + "&businessId=" + businessId;
    String noLink  = "http://52.3.145.159:8080/api/v1/hotel/handle-no?notificationId="
                     + notificationId + "&businessId=" + businessId;

    return "<html><body>"
         + "<h3>High Negative Reviews Alert!</h3>"
         + "<p>Your hotel <strong>" + hotelName + "</strong> has " 
         + String.format("%.2f", negativeRatio)
         + "% negative sentiment analysis from call reviews.</p>"
         + "<p>Would you like us to fetch Google Reviews for more details?</p>"
         + "<a href='" + yesLink + "'>YES, handle</a><br>"
         + "<a href='" + noLink + "'>NO, ignore</a>"
         + "</body></html>";
}


    
}
