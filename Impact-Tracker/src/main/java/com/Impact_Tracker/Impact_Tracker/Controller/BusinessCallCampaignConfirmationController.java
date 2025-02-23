package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignConfirmation;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessCallCampaignConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/call-campaign-confirmation")
public class BusinessCallCampaignConfirmationController {

    @Autowired
    private BusinessCallCampaignConfirmationService callCampaignService;

    // Create or fetch a "Pending" record
    @PostMapping("/create/{businessId}")
    public ResponseEntity<BusinessCallCampaignConfirmation> createPending(
            @PathVariable Long businessId
    ) {
        BusinessCallCampaignConfirmation record = callCampaignService.createPending(businessId);
        return ResponseEntity.ok(record);
    }

    // Update user response (Y or N)
    @PostMapping("/respond")
    public ResponseEntity<String> respond(
            @RequestParam("businessId") Long businessId,
            @RequestParam("userResponse") String userResponse
    ) {
        if (!userResponse.equalsIgnoreCase("Y") && !userResponse.equalsIgnoreCase("N")) {
            return ResponseEntity.badRequest().body("Invalid response. Must be 'Y' or 'N'.");
        }
        callCampaignService.updateUserResponse(businessId, userResponse);
        return ResponseEntity.ok("Call campaign response recorded successfully!");
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<BusinessCallCampaignConfirmation> getConfirmation(
            @PathVariable Long businessId
    ) {
        BusinessCallCampaignConfirmation rec = callCampaignService.getByBusiness(businessId);
        if (rec == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rec);
    }

    
}
