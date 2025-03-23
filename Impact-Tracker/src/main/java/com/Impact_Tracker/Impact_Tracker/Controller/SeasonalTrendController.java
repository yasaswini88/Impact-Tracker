package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.SeasonalTrendDto;
import com.Impact_Tracker.Impact_Tracker.Entity.SeasonalTrend;
import com.Impact_Tracker.Impact_Tracker.Repo.SeasonalTrendRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seasonal-trends")
public class SeasonalTrendController {
    
    @Autowired
    private SeasonalTrendRepository seasonalTrendRepository;

    @GetMapping("/{businessId}")
    public ResponseEntity<SeasonalTrendDto> getLatestTrend(@PathVariable Long businessId) {
        SeasonalTrend trend = seasonalTrendRepository.findTopByBusiness_BusinessIdOrderByGeneratedAtDesc(businessId);
        if (trend == null) return ResponseEntity.notFound().build();

        SeasonalTrendDto dto = new SeasonalTrendDto();
        dto.setBusinessId(businessId);
        dto.setGeneratedAt(trend.getGeneratedAt());

        try {
            dto.setMonthlyTrends(new ObjectMapper().readValue(trend.getMonthlyTrendsJson(), Map.class));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON trends data.", e);
        }

        return ResponseEntity.ok(dto);
    }
}
