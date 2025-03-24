package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.PlansDto;
import com.Impact_Tracker.Impact_Tracker.Service.PlansService;
import org.springframework.beans.factory.annotation.Autowired;
import com.Impact_Tracker.Impact_Tracker.Entity.Plans;
import com.Impact_Tracker.Impact_Tracker.Repo.PlansRepository;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/plans")
public class PlansController {

    private final PlansService plansService;

    @Autowired
    private PlansRepository plansRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    public PlansController(PlansService plansService) {
        this.plansService = plansService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PlansDto> createPlan(@RequestBody PlansDto plansDto) {
        PlansDto createdPlan = plansService.createPlan(plansDto);
        return ResponseEntity.ok(createdPlan);
    }

    // READ (GET by ID)
    @GetMapping("/{id}")
    public ResponseEntity<PlansDto> getPlanById(@PathVariable("id") Long planId) {
        PlansDto plan = plansService.getPlanById(planId);
        return ResponseEntity.ok(plan);
    }

    // READ (GET all) - FIXED endpoint to avoid conflict
    @GetMapping
    public ResponseEntity<List<Plans>> getAllPlans() {
        List<Plans> plans = plansRepository.findAll();
        return ResponseEntity.ok(plans);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PlansDto> updatePlan(
        @PathVariable("id") Long planId,
        @RequestBody PlansDto plansDto
    ) {
        PlansDto updatedPlan = plansService.updatePlan(planId, plansDto);
        return ResponseEntity.ok(updatedPlan);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("id") Long planId) {
        plansService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{businessId}/upgrade-plan/{planId}")
    public ResponseEntity<?> upgradePlan(
        @PathVariable Long businessId,
        @PathVariable Long planId
    ) {
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

        Plans newPlan = plansRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Plan not found"));

        business.setPlan(newPlan);
        business.setDateOfPlanUpdated(LocalDateTime.now());
        businessRepository.save(business);

        return ResponseEntity.ok("Plan upgraded successfully to " + newPlan.getPlanName());
    }
}



