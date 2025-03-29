package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessOperationalFeaturesDto;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessOperationalFeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-operational-features")
public class BusinessOperationalFeaturesController {

    @Autowired
    private BusinessOperationalFeaturesService boFeaturesService;

    
    @PostMapping
    public ResponseEntity<BusinessOperationalFeaturesDto> createFeature(@RequestBody BusinessOperationalFeaturesDto dto) {
        BusinessOperationalFeaturesDto created = boFeaturesService.createFeature(dto);
        return ResponseEntity.ok(created);
    }

    // GET all operational feature records
    @GetMapping
    public ResponseEntity<List<BusinessOperationalFeaturesDto>> getAllFeatures() {
        List<BusinessOperationalFeaturesDto> list = boFeaturesService.getAllFeatures();
        return ResponseEntity.ok(list);
    }

    // GET a single operational feature record by ID
    @GetMapping("/{id}")
    public ResponseEntity<BusinessOperationalFeaturesDto> getFeatureById(@PathVariable Long id) {
        BusinessOperationalFeaturesDto dto = boFeaturesService.getFeatureById(id);
        return ResponseEntity.ok(dto);
    }

    // UPDATE an operational feature record by ID
    @PutMapping("/{id}")
    public ResponseEntity<BusinessOperationalFeaturesDto> updateFeature(@PathVariable Long id,
                                                                        @RequestBody BusinessOperationalFeaturesDto dto) {
        BusinessOperationalFeaturesDto updated = boFeaturesService.updateFeature(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE an operational feature record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        boFeaturesService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}
