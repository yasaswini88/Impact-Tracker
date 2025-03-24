package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.FacebookReview;
import com.Impact_Tracker.Impact_Tracker.Entity.FacebookReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.FacebookReviewRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.FacebookReviewInsightsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacebookReviewInsightsService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private FacebookReviewRepository facebookReviewRepository;

    @Autowired
    private FacebookReviewInsightsRepository facebookReviewInsightsRepository;

    @Autowired
    private OpenAiService openAiService;

    public FacebookReviewInsights generateInsightsForBusiness(Long businessId) throws Exception {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID=" + businessId));

        List<FacebookReview> reviews = facebookReviewRepository.findAll()
                .stream()
                .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
                .toList();

        if (reviews.isEmpty()) {
            throw new RuntimeException("No Facebook reviews found for business ID=" + businessId);
        }

        List<String> reviewTexts = reviews.stream()
                .map(FacebookReview::getReviewText)
                .toList();

        String openAiResponse = openAiService.analyzeReviews(reviewTexts);

        int firstBraceIndex = openAiResponse.indexOf("{");
        if (firstBraceIndex != -1) {
            openAiResponse = openAiResponse.substring(firstBraceIndex);
        }
        String cleanedResponse = openAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(cleanedResponse);
        JsonNode insightsNode = root.path("insights");

        String positivePoints = insightsNode.has("positive")
                ? insightsNode.path("positive").asText("")
                : insightsNode.path("positive_points").asText("");

        String negativePoints = insightsNode.has("negative")
                ? insightsNode.path("negative").asText("")
                : insightsNode.path("negative_points").asText("");

        String overallSummary = insightsNode.has("overall_summary")
                ? insightsNode.path("overall_summary").asText("")
                : insightsNode.path("summary").asText("");

        FacebookReviewInsights insightsEntity = new FacebookReviewInsights();
        insightsEntity.setBusiness(business);
        insightsEntity.setPositivePoints(positivePoints);
        insightsEntity.setNegativePoints(negativePoints);
        insightsEntity.setInsights(overallSummary);
        insightsEntity.setAnalysisDate(LocalDateTime.now());

        return facebookReviewInsightsRepository.save(insightsEntity);
    }

    public FacebookReviewInsights getInsightsForBusiness(Long businessId) {
        return facebookReviewInsightsRepository.findAll().stream()
                .filter(x -> x.getBusiness().getBusinessId().equals(businessId))
                .findFirst()
                .orElse(null);
    }
}
