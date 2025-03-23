package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.FacebookReview;
import com.Impact_Tracker.Impact_Tracker.Service.FacebookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facebook-reviews")
public class FacebookReviewController {

    @Autowired
    private FacebookReviewService facebookReviewService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchFacebookReviews(
            @RequestParam Long businessId,
            @RequestParam String datasetUrl) {
        facebookReviewService.fetchAndStoreFacebookReviews(businessId, datasetUrl);
        return ResponseEntity.ok("Fetched and stored Facebook reviews for business " + businessId);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<FacebookReview>> getFacebookReviews(@PathVariable Long businessId) {
        List<FacebookReview> reviews = facebookReviewService.getReviewsByBusinessId(businessId);
        return ResponseEntity.ok(reviews);
    }
}
