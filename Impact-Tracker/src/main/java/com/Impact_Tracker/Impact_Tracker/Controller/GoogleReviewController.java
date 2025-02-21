package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.GoogleReview;
import com.Impact_Tracker.Impact_Tracker.Service.GoogleReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/google-reviews")
public class GoogleReviewController {

    @Autowired
    private GoogleReviewService googleReviewService;

    @PostMapping("/fetch/{businessId}")
    public ResponseEntity<String> fetchReviews(@PathVariable Long businessId) {
        googleReviewService.fetchReviewsForBusiness(businessId);
        return ResponseEntity.ok("Triggered Apify fetch for business " + businessId);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<GoogleReview>> getReviewsByBusiness(@PathVariable Long businessId) {
        List<GoogleReview> list = googleReviewService.getReviewsByBusinessId(businessId);
        return ResponseEntity.ok(list);
    }
}
