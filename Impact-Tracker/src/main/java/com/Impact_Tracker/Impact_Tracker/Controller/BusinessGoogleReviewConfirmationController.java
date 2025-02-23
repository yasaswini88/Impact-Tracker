package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessGoogleReviewConfirmation;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessGoogleReviewConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review-confirmation")
public class BusinessGoogleReviewConfirmationController {

    @Autowired
    private BusinessGoogleReviewConfirmationService confirmationService;

    // 1) Create a "pending" record for a business 
    //    (optional if you want to do this automatically in code)
    @PostMapping("/create/{businessId}")
    public ResponseEntity<BusinessGoogleReviewConfirmation> createPending(@PathVariable Long businessId) {
        BusinessGoogleReviewConfirmation record = 
            confirmationService.createPendingConfirmation(businessId);
        return ResponseEntity.ok(record);
    }

    // 2) Update the user’s response to "Y" or "N"
    @PostMapping("/respond")
    public ResponseEntity<String> updateResponse(
            @RequestParam("businessId") Long businessId,
            @RequestParam("userResponse") String userResponse
    ) {
        if (!userResponse.equalsIgnoreCase("Y") && !userResponse.equalsIgnoreCase("N")) {
            return ResponseEntity.badRequest().body("Invalid response. Must be 'Y' or 'N'.");
        }
        confirmationService.updateConfirmationResponse(businessId, userResponse);
        return ResponseEntity.ok("Response recorded successfully!");
    }

    // 3) Fetch the current record to see if user responded or not
    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessGoogleReviewConfirmation> getConfirmation(@PathVariable Long businessId) {
        BusinessGoogleReviewConfirmation record = confirmationService.getConfirmation(businessId);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }
}
