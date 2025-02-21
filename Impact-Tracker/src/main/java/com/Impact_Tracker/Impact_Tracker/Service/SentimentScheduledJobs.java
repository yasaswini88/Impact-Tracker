package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.SentimentAnalysis;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.SentimentAnalysisRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Service.HotelNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SentimentScheduledJobs {

    @Autowired
    private SentimentAnalysisRepository sentimentAnalysisRepository;

    @Autowired
    private BusinessRepository businessRepository;
    
    @Autowired
    private HotelNotificationService notificationService; 
    // This could be your Twilio or email service, or both.

    /**
     * Scheduled method to check unhandled sentiments. 
     * Runs daily at 2:00 AM (just an example).
     * CRON format: second, minute, hour, dayOfMonth, month, dayOfWeek
     */
    @Scheduled(cron = "0 0 2 * * *")
    // @Scheduled(cron = "0 0/2 * * * ?")  // Run every 2 minutes for testing
    public void checkNegativeSentiments() {
        // 1) Fetch all SentimentAnalysis with aiHandled = false
        List<SentimentAnalysis> unhandledList = sentimentAnalysisRepository.findAll()
            .stream()
            .filter(sa -> sa.getAiHandled() != null && !sa.getAiHandled())
            .collect(Collectors.toList());

        if (unhandledList.isEmpty()) {
            return; // Nothing to do
        }

        // 2) Group them by Business
        Map<Business, List<SentimentAnalysis>> groupedByBusiness =
                unhandledList.stream()
                             .collect(Collectors.groupingBy(SentimentAnalysis::getBusiness));

        // 3) For each business, compute negative ratio and possibly notify
        for (Map.Entry<Business, List<SentimentAnalysis>> entry : groupedByBusiness.entrySet()) {
            Business business = entry.getKey();
            List<SentimentAnalysis> sentimentsForBiz = entry.getValue();

            // Optional: Filter only "Negative" sentiment
            long negativeCount = sentimentsForBiz.stream()
                    .filter(sa -> "Negative".equalsIgnoreCase(sa.getSentiment()))
                    .count();
            long totalCount = sentimentsForBiz.size();

            double negativePercentage = ((double) negativeCount / totalCount) * 100.0;

            // 4) If negative > 30%, notify
            if (negativePercentage > 30.0) {
                // Example check if businessType == "hotel" or "lawn mowing"
                // (assuming you have a 'businessType' field in Business entity)
                String bizType = business.getBusinessType(); 
                if ("hotel".equalsIgnoreCase(bizType) || "lawn mowing".equalsIgnoreCase(bizType)) {
                    // This is your custom logic to notify:
                    // e.g. email, Twilio call, etc.
                    // notificationService.notifyBusinessUser(business, negativePercentage);
                }

                // 5) Mark these sentiments as handled so we don't repeat
                for (SentimentAnalysis sa : sentimentsForBiz) {
                    sa.setAiHandled(true);
                }
                // Save them in batch
                sentimentAnalysisRepository.saveAll(sentimentsForBiz);
            }
        }
    }
}
