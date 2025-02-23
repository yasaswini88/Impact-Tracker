package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignStrategies;
import com.Impact_Tracker.Impact_Tracker.Service.CallCampaignStrategiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sample endpoints to manage call campaign strategies.
 */
@RestController
@RequestMapping("/api/v1/call-campaign-strategies")
public class CallCampaignStrategiesController {

    @Autowired
    private CallCampaignStrategiesService strategiesService;

    // 1) CREATE
  // Controller
@PostMapping
public CallCampaignStrategies create(@RequestBody CallCampaignStrategies request) {
    return strategiesService.createStrategy(
        request.getStrategyName(),
        request.getDescription()
    );
}


    // 2) GET ALL
    @GetMapping
    public List<CallCampaignStrategies> getAllStrategies() {
        return strategiesService.getAllStrategies();
    }

    // 3) GET by ID
    @GetMapping("/{id}")
    public CallCampaignStrategies getStrategyById(@PathVariable Long id) {
        return strategiesService.getById(id);
    }

    // 4) UPDATE
    @PutMapping("/{id}")
    public CallCampaignStrategies updateStrategy(@PathVariable Long id,
                                                 @RequestBody CallCampaignStrategies request) {
        return strategiesService.updateStrategy(id, request.getStrategyName());
    }

    // 5) DELETE
    @DeleteMapping("/{id}")
    public void deleteStrategy(@PathVariable Long id) {
        strategiesService.deleteStrategy(id);
    }
}
