package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.FeatureDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Feature;
import com.Impact_Tracker.Impact_Tracker.Repo.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    public FeatureDto createFeature(FeatureDto dto) {
        Feature feature = new Feature();
        feature.setFeatureName(dto.getFeatureName());
        Feature saved = featureRepository.save(feature);
        return mapToDto(saved);
    }

    public FeatureDto getFeatureById(Long id) {
        Feature feature = featureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Feature not found with id=" + id));
        return mapToDto(feature);
    }

    public List<FeatureDto> getAllFeatures() {
        return featureRepository.findAll()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public FeatureDto updateFeature(Long id, FeatureDto dto) {
        Feature existingFeature = featureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Feature not found with id=" + id));
        
        existingFeature.setFeatureName(dto.getFeatureName());

        Feature updated = featureRepository.save(existingFeature);
        return mapToDto(updated);
    }

    public void deleteFeature(Long id) {
        if (!featureRepository.existsById(id)) {
            throw new RuntimeException("Feature not found with id=" + id);
        }
        featureRepository.deleteById(id);
    }

    private FeatureDto mapToDto(Feature feature) {
        FeatureDto dto = new FeatureDto();
        dto.setFeatureId(feature.getFeatureId());
        dto.setFeatureName(feature.getFeatureName());
        dto.setCreatedTime(feature.getCreatedTime().toString());
        return dto;
    }
}
