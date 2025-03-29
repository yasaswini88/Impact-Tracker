package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReview;
import com.Impact_Tracker.Impact_Tracker.Service.MapCommunicationReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/map-communication-reviews")
public class MapCommunicationReviewController {

    @Autowired
    private MapCommunicationReviewService reviewService;

    @PostMapping("/fetch/{mapCommunicationId}")
    public ResponseEntity<String> fetchReviews(@PathVariable Long mapCommunicationId) {
        try {
            reviewService.fetchAndStoreMapCommunicationReviews(mapCommunicationId);
            return ResponseEntity.ok("Triggered Apify fetch for Map Communication ID " + mapCommunicationId);
        } catch (InterruptedException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{mapCommunicationId}")
    public ResponseEntity<List<MapCommunicationReview>> getReviews(@PathVariable Long mapCommunicationId) {
        List<MapCommunicationReview> reviews = reviewService.getReviewsByMapCommunicationId(mapCommunicationId);
        return ResponseEntity.ok(reviews);
    }
}
