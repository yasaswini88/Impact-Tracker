package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Service.MapCommunicationReviewInsightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/map-review-insights")
public class MapCommunicationReviewInsightsController {

    @Autowired
    private MapCommunicationReviewInsightsService insightsService;

    @PostMapping("/generate/{mapCommunicationId}")
    public ResponseEntity<String> generateInsights(@PathVariable Long mapCommunicationId) {
        try {
            MapCommunicationReviewInsights savedInsights = insightsService.generateInsightsForMapCommunication(mapCommunicationId);
            return ResponseEntity.ok("Insights generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{mapCommunicationId}")
    public ResponseEntity<MapCommunicationReviewInsights> getInsights(@PathVariable Long mapCommunicationId) {
        MapCommunicationReviewInsights insights = insightsService.getInsightsForMapCommunication(mapCommunicationId);
        if (insights != null) {
            return ResponseEntity.ok(insights);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
