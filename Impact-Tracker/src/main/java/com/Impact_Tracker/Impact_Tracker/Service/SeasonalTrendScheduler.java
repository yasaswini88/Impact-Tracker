package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.SeasonalTrend;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.SeasonalTrendRepository;
// import com.Impact_Tracker.Impact_Tracker.Service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeasonalTrendScheduler {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private OpenAiService openAiService;

    @Autowired
    private SeasonalTrendRepository seasonalTrendRepository;

    @Scheduled(cron = "0 0 0 * * SUN") // Every Sunday at midnight
    // @Scheduled(cron = "0 0/2 * * * ?")
    public void generateSeasonalTrendsJob() {
        List<Business> businesses = businessRepository.findAll();
        for (Business business : businesses) {
            String trendsJson = openAiService.generateSeasonalTrends(
                    business.getBusinessType(),
                    business.getGooglePlacesLink()
            );
            SeasonalTrend trend = new SeasonalTrend();
            trend.setBusiness(business);
            trend.setMonthlyTrendsJson(trendsJson);
            seasonalTrendRepository.save(trend);
        }
    }
}
