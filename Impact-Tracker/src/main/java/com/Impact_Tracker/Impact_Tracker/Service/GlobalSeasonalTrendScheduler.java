package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.GlobalSeasonalTrend;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.GlobalSeasonalTrendRepository;
import com.Impact_Tracker.Impact_Tracker.Service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class GlobalSeasonalTrendScheduler {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private GlobalSeasonalTrendRepository globalSeasonalTrendRepository;

    @Autowired
    private OpenAiService openAiService;

    // @Scheduled(cron = "0 0 1 * * SUN") // Every Sunday at 1 AM
    // @Scheduled(cron = "0 0/1 * * * ?")
    public void generateGlobalSeasonalTrends() {
        List<String> businessTypes = businessRepository.findAll()
            .stream()
            .map(Business::getBusinessType)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        for (String type : businessTypes) {
            String trendsJson = openAiService.generateGlobalSeasonalTrends(type, "USA");

            GlobalSeasonalTrend trend = globalSeasonalTrendRepository.findByBusinessType(type);
            if (trend == null) {
                trend = new GlobalSeasonalTrend();
                trend.setBusinessType(type);
            }

            trend.setMonthlyTrendsJson(trendsJson);
            globalSeasonalTrendRepository.save(trend);
        }
    }
}
