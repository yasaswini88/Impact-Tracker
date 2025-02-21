package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.GoogleReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Service.GoogleReviewInsightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/insights")
public class GoogleReviewInsightsController {

    @Autowired
    private GoogleReviewInsightsService googleReviewInsightsService;

    // POST or GET, depending on how you want to trigger it
@PostMapping("/generate/{businessId}")
public ResponseEntity<String> generateInsights(
        @PathVariable("businessId") Long businessId
) {
    try {
        GoogleReviewInsights savedInsights =
                googleReviewInsightsService.generateInsightsForBusiness(businessId);
        return ResponseEntity.ok("Insights generated successfully.");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}



@GetMapping("/{businessId}")
public ResponseEntity<GoogleReviewInsights> getInsights(@PathVariable("businessId") Long businessId) {
    try {
        GoogleReviewInsights insights = googleReviewInsightsService.getInsightsForBusiness(businessId);
        if (insights != null) {
            return ResponseEntity.ok(insights);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}


   
}
