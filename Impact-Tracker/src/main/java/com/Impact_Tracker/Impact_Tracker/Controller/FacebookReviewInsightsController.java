package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.FacebookReviewInsights;
import com.Impact_Tracker.Impact_Tracker.Service.FacebookReviewInsightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facebook-insights")
public class FacebookReviewInsightsController {

    @Autowired
    private FacebookReviewInsightsService facebookReviewInsightsService;

    @PostMapping("/generate/{businessId}")
    public ResponseEntity<String> generateInsights(@PathVariable Long businessId) {
        try {
            facebookReviewInsightsService.generateInsightsForBusiness(businessId);
            return ResponseEntity.ok("Facebook review insights generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<FacebookReviewInsights> getInsights(@PathVariable Long businessId) {
        FacebookReviewInsights insights = facebookReviewInsightsService.getInsightsForBusiness(businessId);
        if (insights != null) {
            return ResponseEntity.ok(insights);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
 