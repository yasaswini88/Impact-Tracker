package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.FeatureDto;
import com.Impact_Tracker.Impact_Tracker.Service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    // CREATE
    @PostMapping
    public ResponseEntity<FeatureDto> createFeature(@RequestBody FeatureDto dto) {
        FeatureDto created = featureService.createFeature(dto);
        return ResponseEntity.ok(created);
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<FeatureDto> getFeatureById(@PathVariable Long id) {
        FeatureDto dto = featureService.getFeatureById(id);
        return ResponseEntity.ok(dto);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<FeatureDto>> getAllFeatures() {
        List<FeatureDto> features = featureService.getAllFeatures();
        return ResponseEntity.ok(features);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FeatureDto> updateFeature(@PathVariable Long id, @RequestBody FeatureDto dto) {
        FeatureDto updated = featureService.updateFeature(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}
