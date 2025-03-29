package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessOperationalFeaturesDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessOperationalFeatures;
import com.Impact_Tracker.Impact_Tracker.Entity.Feature;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessOperationalFeaturesRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessOperationalFeaturesService {

    @Autowired
    private BusinessOperationalFeaturesRepository boFeaturesRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public BusinessOperationalFeaturesDto createFeature(BusinessOperationalFeaturesDto dto) {
        // Find the Business entity
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));

        // Find the Feature entity
        Feature feature = featureRepository.findById(dto.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found with ID: " + dto.getFeatureId()));

        // If no enabled date provided, set to current date
        LocalDate enabledDate = (dto.getFeatureEnabled() != null) ? dto.getFeatureEnabled() : LocalDate.now();

        BusinessOperationalFeatures boFeature = new BusinessOperationalFeatures();
        boFeature.setBusiness(business);
        boFeature.setFeature(feature);
        boFeature.setFeatureEnabled(enabledDate);

        BusinessOperationalFeatures saved = boFeaturesRepository.save(boFeature);
        return mapToDto(saved);
    }

    public List<BusinessOperationalFeaturesDto> getAllFeatures() {
        return boFeaturesRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BusinessOperationalFeaturesDto getFeatureById(Long id) {
        BusinessOperationalFeatures boFeature = boFeaturesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + id));
        return mapToDto(boFeature);
    }

    public BusinessOperationalFeaturesDto updateFeature(Long id, BusinessOperationalFeaturesDto dto) {
        BusinessOperationalFeatures boFeature = boFeaturesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + id));

        if (dto.getBusinessId() != null) {
            Business business = businessRepository.findById(dto.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));
            boFeature.setBusiness(business);
        }

        if (dto.getFeatureId() != null) {
            Feature feature = featureRepository.findById(dto.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found with ID: " + dto.getFeatureId()));
            boFeature.setFeature(feature);
        }

        if (dto.getFeatureEnabled() != null) {
            boFeature.setFeatureEnabled(dto.getFeatureEnabled());
        }

        BusinessOperationalFeatures updated = boFeaturesRepository.save(boFeature);
        return mapToDto(updated);
    }

    public void deleteFeature(Long id) {
        if (!boFeaturesRepository.existsById(id)) {
            throw new RuntimeException("Record not found with ID: " + id);
        }
        boFeaturesRepository.deleteById(id);
    }

    // Helper method to convert entity to DTO
    private BusinessOperationalFeaturesDto mapToDto(BusinessOperationalFeatures boFeature) {
        BusinessOperationalFeaturesDto dto = new BusinessOperationalFeaturesDto();
        dto.setId(boFeature.getId());
        dto.setBusinessId(boFeature.getBusiness().getBusinessId());
        dto.setFeatureId(boFeature.getFeature().getFeatureId());
        dto.setFeatureEnabled(boFeature.getFeatureEnabled());
        return dto;
    }
}
