package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.GoogleReview;
import com.Impact_Tracker.Impact_Tracker.Entity.GoogleReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.GoogleReviewRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.GoogleReviewInsightsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class GoogleReviewInsightsService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private GoogleReviewRepository googleReviewRepository;

    @Autowired
    private GoogleReviewInsightsRepository googleReviewInsightsRepository;

    @Autowired
    private OpenAiService openAiService;

    /**
     * 1) Gather all reviews for the business,
     * 2) Send to OpenAI,
     * 3) Parse the JSON response,
     * 4) Store in google_review_insights table.
     */
    public GoogleReviewInsights generateInsightsForBusiness(Long businessId) throws Exception {
        System.out.println("Starting insight generation for business ID: " + businessId);

        // Get the Business
        System.out.println("Fetching business details from the database...");
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));
        System.out.println("Business found: " + business.getBusinessName());

        // Gather all GoogleReview text
        System.out.println("Fetching all reviews for business ID: " + businessId);
        List<GoogleReview> reviews = googleReviewRepository.findAll()
                .stream()
                .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
                .toList();

        if (reviews.isEmpty()) {
            System.out.println("No reviews found for business ID: " + businessId);
            throw new RuntimeException("No reviews found for business ID=" + businessId);
        }
        System.out.println("Total reviews fetched: " + reviews.size());

        // Extract the text
        System.out.println("Extracting review texts...");
        List<String> reviewTexts = reviews.stream()
                .map(GoogleReview::getReviewText)
                .toList();
        System.out.println("Total review texts extracted: " + reviewTexts.size());

        // Call OpenAI
        System.out.println("Sending reviews to OpenAI for analysis...");
        String openAiResponse = openAiService.analyzeReviews(reviewTexts);
        // String cleanedResponse = openAiResponse.replaceAll("```", "").trim();
        // Ensure JSON starts at the correct position
int firstBraceIndex = openAiResponse.indexOf("{");
if (firstBraceIndex != -1) {
    openAiResponse = openAiResponse.substring(firstBraceIndex); 
}

// Remove Markdown-style code blocks if present
String cleanedResponse = openAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

System.out.println("Cleaned OpenAI Response: " + cleanedResponse);
        System.out.println("Received response from OpenAI.");

        // Parse the JSON
       System.out.println("Parsing JSON response from OpenAI...");
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


System.out.println("Parsed insights from JSON:");
System.out.println("Positive Points: " + positivePoints);
System.out.println("Negative Points: " + negativePoints);
System.out.println("Overall Summary: " + overallSummary);


        // Construct and save an entity
        System.out.println("Creating GoogleReviewInsights entity...");
        GoogleReviewInsights insightsEntity = new GoogleReviewInsights();
        insightsEntity.setBusiness(business);
       
        insightsEntity.setPositivePoints(positivePoints);
        insightsEntity.setNegativePoints(negativePoints);
        insightsEntity.setInsights(overallSummary);

        insightsEntity.setAnalysisDate(LocalDateTime.now());

        // Save to DB
        System.out.println("Saving insights to the database...");
        GoogleReviewInsights savedInsights = googleReviewInsightsRepository.save(insightsEntity);
        System.out.println("Insights saved successfully for business ID: " + businessId);

        return savedInsights;
    }

    public GoogleReviewInsights getInsightsForBusiness(Long businessId) {
        System.out.println("Fetching insights for business ID: " + businessId);
        GoogleReviewInsights insights = googleReviewInsightsRepository.findAll().stream()
                .filter(x -> x.getBusiness().getBusinessId().equals(businessId))
                .findFirst()
                .orElse(null);

        if (insights != null) {
            System.out.println("Insights found for business ID: " + businessId);
        } else {
            System.out.println("No insights found for business ID: " + businessId);
        }

        return insights;
    }
}
