package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.PlansDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Plans;
import com.Impact_Tracker.Impact_Tracker.Repo.PlansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlansService {

    @Autowired
    private PlansRepository plansRepository;

    public PlansDto createPlan(PlansDto plansDto) {
        Plans plan = mapToEntity(plansDto);
        plan.setCreatedTime(LocalDateTime.now());
        plan.setUpdatedTime(LocalDateTime.now());
        Plans savedPlan = plansRepository.save(plan);
        return mapToDto(savedPlan);
    }

    public PlansDto getPlanById(Long planId) {
        Plans plan = plansRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + planId));
        return mapToDto(plan);
    }

    public List<PlansDto> getAllPlans() {
        List<Plans> plansList = plansRepository.findAll();
        return plansList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public PlansDto updatePlan(Long planId, PlansDto plansDto) {
        Plans existingPlan = plansRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + planId));

        existingPlan.setPlanName(plansDto.getPlanName());
        existingPlan.setUpdatedTime(LocalDateTime.now());

        Plans updatedPlan = plansRepository.save(existingPlan);
        return mapToDto(updatedPlan);
    }

    public void deletePlan(Long planId) {
        if (!plansRepository.existsById(planId)) {
            throw new RuntimeException("Plan not found with ID: " + planId);
        }
        plansRepository.deleteById(planId);
    }

    private PlansDto mapToDto(Plans plan) {
        PlansDto dto = new PlansDto();
        dto.setPlanId(plan.getPlanId());
        dto.setPlanName(plan.getPlanName());
        dto.setCreatedTime(plan.getCreatedTime() != null ? plan.getCreatedTime().toString() : null);
        dto.setUpdatedTime(plan.getUpdatedTime() != null ? plan.getUpdatedTime().toString() : null);
        return dto;
    }

    private Plans mapToEntity(PlansDto dto) {
        Plans plan = new Plans();
        plan.setPlanName(dto.getPlanName());
        return plan;
    }
}
