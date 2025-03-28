

package com.Impact_Tracker.Impact_Tracker.Controller;

// import com.Impact_Tracker.Impact_Tracker.Entity.BusinessCallCampaignConfirmation;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessCallCampaignConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessGoogleReviewConfirmationService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.HashMap;   
@RestController
@RequestMapping("/api/v1/confirmations")
public class ConfirmationsController {

  @Autowired
  private BusinessGoogleReviewConfirmationService googleReviewService;

  @Autowired
  private BusinessCallCampaignConfirmationService callCampaignService;

  @GetMapping("/state")
  public ResponseEntity<Map<String,Object>> getConfirmationState(@RequestParam Long businessId) {
     Map<String,Object> state = new HashMap<>();
     
     var googleConf = googleReviewService.getConfirmation(businessId);
     // If null => means not created yet
     state.put("googleReviewResponse", googleConf != null ? googleConf.getUserResponse() : "NONE");

     var callConf = callCampaignService.getByBusiness(businessId);
     state.put("callCampaignResponse", callConf != null ? callConf.getUserResponse() : "NONE");

     return ResponseEntity.ok(state);
  }
}
