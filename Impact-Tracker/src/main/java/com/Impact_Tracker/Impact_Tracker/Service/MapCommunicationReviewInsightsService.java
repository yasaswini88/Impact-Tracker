package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunication;
import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReview;
import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationReviewInsightsRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationReviewRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MapCommunicationReviewInsightsService {

    @Autowired
    private MapCommunicationRepository mapCommunicationRepository;

    @Autowired
    private MapCommunicationReviewRepository reviewRepository;

    @Autowired
    private MapCommunicationReviewInsightsRepository insightsRepository;

    @Autowired
    private OpenAiService openAiService; // Reuse your existing OpenAI integration service

    public MapCommunicationReviewInsights generateInsightsForMapCommunication(Long mapCommunicationId) throws Exception {
        // Fetch the MapCommunication record
        MapCommunication mapComm = mapCommunicationRepository.findById(mapCommunicationId)
            .orElseThrow(() -> new RuntimeException("Map Communication not found with ID: " + mapCommunicationId));

        // Gather all reviews for this Map Communication
        List<MapCommunicationReview> reviews = reviewRepository.findAll().stream()
            .filter(r -> r.getMapCommunication().getId().equals(mapCommunicationId))
            .toList();

        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for Map Communication ID=" + mapCommunicationId);
        }

        // Extract review texts
        List<String> reviewTexts = reviews.stream()
            .map(MapCommunicationReview::getReviewText)
            .toList();

        // Call OpenAI to analyze the reviews
        String openAiResponse = openAiService.analyzeReviews(reviewTexts);
        int firstBraceIndex = openAiResponse.indexOf("{");
        if (firstBraceIndex != -1) {
            openAiResponse = openAiResponse.substring(firstBraceIndex);
        }
        String cleanedResponse = openAiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

        // Parse the JSON response
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

        // Create and save the insights entity
        MapCommunicationReviewInsights insightsEntity = new MapCommunicationReviewInsights();
        insightsEntity.setMapCommunication(mapComm);
        insightsEntity.setPositivePoints(positivePoints);
        insightsEntity.setNegativePoints(negativePoints);
        insightsEntity.setInsights(overallSummary);
        insightsEntity.setAnalysisDate(LocalDateTime.now());

        return insightsRepository.save(insightsEntity);
    }

    public MapCommunicationReviewInsights getInsightsForMapCommunication(Long mapCommunicationId) {
        return insightsRepository.findAll().stream()
            .filter(x -> x.getMapCommunication().getId().equals(mapCommunicationId))
            .findFirst()
            .orElse(null);
    }
}
