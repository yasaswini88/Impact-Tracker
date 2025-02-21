package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Service.GoogleReviewService;
import com.Impact_Tracker.Impact_Tracker.Service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review-insights")
public class ReviewAnalysisController { 

    @Autowired
    private GoogleReviewService googleReviewService;

    @Autowired
    private OpenAiService openAiService;


    @GetMapping("/{businessId}")
    public ResponseEntity<String> getReviewInsights(@PathVariable Long businessId) {

        List<String> reviews = googleReviewService.getReviewsTextByBusinessId(businessId);

       
        String insights = openAiService.analyzeReviews(reviews);

       
        return ResponseEntity.ok(insights);
    }
}
