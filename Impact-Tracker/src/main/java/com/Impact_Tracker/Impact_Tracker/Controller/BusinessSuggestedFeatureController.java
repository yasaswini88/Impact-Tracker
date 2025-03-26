package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.BusinessSuggestedFeature;
import com.Impact_Tracker.Impact_Tracker.Entity.Feature;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessSuggestedFeatureService;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessSuggestedFeatureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/business-feature-suggestions")
public class BusinessSuggestedFeatureController {

    @Autowired
    private BusinessSuggestedFeatureService service;

    @Autowired
    private BusinessSuggestedFeatureRepository suggestedFeatureRepository;

    /**
     * Endpoint to generate feature suggestions for a business.
     * Requires a JSON body with "callVolumeSummary".
     *
     * Example request:
     * POST /api/v1/business-feature-suggestions/generate/6
     * Body: { "callVolumeSummary": "Calls are decreasing due to XYZ..." }
     */
    @PostMapping("/generate/{businessId}")
    public ResponseEntity<String> generateFeatureSuggestions(
        @PathVariable Long businessId,
        @RequestBody Map<String, String> body
    ) {
        try {
            String callVolumeSummary = body.get("callVolumeSummary");
            if (callVolumeSummary == null || callVolumeSummary.isBlank()) {
                return ResponseEntity.badRequest().body("Missing callVolumeSummary in request body.");
            }

            service.generateSuggestedFeatures(businessId, callVolumeSummary);
            return ResponseEntity.ok("Feature suggestions generated and stored.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve all suggested features for a business.
     *
     * Example: GET /api/v1/business-feature-suggestions/6
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<List<Feature>> getSuggestedFeatures(@PathVariable Long businessId) {
        List<BusinessSuggestedFeature> suggestions = suggestedFeatureRepository
            .findByBusiness_BusinessId(businessId);

        List<Feature> features = suggestions.stream()
            .map(BusinessSuggestedFeature::getFeature)
            .collect(Collectors.toList());

        return ResponseEntity.ok(features);
    }
}
