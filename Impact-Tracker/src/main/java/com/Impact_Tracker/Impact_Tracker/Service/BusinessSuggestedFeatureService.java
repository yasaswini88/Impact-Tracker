package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.*;
import com.Impact_Tracker.Impact_Tracker.Repo.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BusinessSuggestedFeatureService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private BusinessSuggestedFeatureRepository suggestedFeatureRepository;

    @Autowired
    private GoogleReviewInsightsRepository googleReviewInsightsRepository;

    @Autowired
    private FacebookReviewInsightsRepository facebookReviewInsightsRepository;

    @Autowired
    private OpenAiService openAiService;



    // @Scheduled(cron = "0 0/2 * * * ?")
    public void generateSuggestedFeatures(Long businessId, String callVolumeSummary) throws Exception {
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

        String businessType = business.getBusinessType();

        // Fetch insights
        GoogleReviewInsights googleInsights = googleReviewInsightsRepository.findAll().stream()
            .filter(g -> g.getBusiness().getBusinessId().equals(businessId))
            .findFirst().orElseThrow(() -> new RuntimeException("Google Insights not found"));

        FacebookReviewInsights fbInsights = facebookReviewInsightsRepository.findAll().stream()
            .filter(f -> f.getBusiness().getBusinessId().equals(businessId))
            .findFirst().orElseThrow(() -> new RuntimeException("Facebook Insights not found"));

        List<String> allFeatures = featureRepository.findAll().stream()
            .map(Feature::getFeatureName).toList();

        // Call OpenAI
        String aiResponse = openAiService.suggestFeatures(
            businessType,
            googleInsights.getInsights(),
            fbInsights.getInsights(),
            callVolumeSummary,
            allFeatures
        );

        ObjectMapper mapper = new ObjectMapper();
        List<String> suggestedFeatureNames = mapper.readValue(aiResponse, new TypeReference<>(){});

        // Store suggestions in DB
        for (String featureName : suggestedFeatureNames) {
            Feature feature = featureRepository.findAll().stream()
                .filter(f -> f.getFeatureName().equalsIgnoreCase(featureName))
                .findFirst()
                .orElse(null);

            if (feature != null) {
                BusinessSuggestedFeature suggestion = new BusinessSuggestedFeature(business, feature);
                suggestedFeatureRepository.save(suggestion);
            }
        }
    }
}
