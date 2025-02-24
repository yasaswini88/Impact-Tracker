package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.CampaignSelectionRequest;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessCallCampaignStrategySelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignStrategySelection;
import java.util.List;


/**
 * This controller handles the user’s call campaign form submissions,
 * storing multiple chosen strategies, plus voice & target audience.
 */


@RestController
@RequestMapping("/api/v1/call-campaign-selection")
public class BusinessCallCampaignStrategySelectionController {

    @Autowired
    private BusinessCallCampaignStrategySelectionService selectionService;

    /**
     * Accepts a JSON body with:
     * {
     *   "businessId": 3,
     *   "strategyIds": [4,5],
     *   "callCampaignVoice": "some text",
     *   "targetAudience": "NEGATIVE"
     * }
     */
    @PostMapping
    public ResponseEntity<String> createSelections(
        @RequestBody CampaignSelectionRequest request
    ) {
        selectionService.saveSelections(request);
        return ResponseEntity.ok("Call Campaign Selection saved successfully!");
    }

    @GetMapping("/by-business/{businessId}")
    public List<BusinessCallCampaignStrategySelection> getSubmissionsByBusiness(
        @PathVariable Long businessId
    ) {
        return selectionService.getSubmissionsForBusiness(businessId);
    }
}
