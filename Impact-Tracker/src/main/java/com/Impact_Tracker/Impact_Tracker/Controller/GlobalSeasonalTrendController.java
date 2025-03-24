package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.GlobalSeasonalTrendDto;
import com.Impact_Tracker.Impact_Tracker.Entity.GlobalSeasonalTrend;
import com.Impact_Tracker.Impact_Tracker.Repo.GlobalSeasonalTrendRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/global-seasonal-trends")
public class GlobalSeasonalTrendController {

    @Autowired
    private GlobalSeasonalTrendRepository globalSeasonalTrendRepository;

    @GetMapping("/{businessType}")
    public ResponseEntity<GlobalSeasonalTrendDto> getTrend(@PathVariable String businessType) {
        GlobalSeasonalTrend trend = globalSeasonalTrendRepository.findByBusinessType(businessType);
        if (trend == null) return ResponseEntity.notFound().build();

        GlobalSeasonalTrendDto dto = new GlobalSeasonalTrendDto();
        dto.setBusinessType(businessType);
        dto.setGeneratedAt(trend.getGeneratedAt());

        try {
            dto.setMonthlyTrends(new ObjectMapper().readValue(trend.getMonthlyTrendsJson(), Map.class));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON trends data.", e);
        }

        return ResponseEntity.ok(dto);
    }
}
