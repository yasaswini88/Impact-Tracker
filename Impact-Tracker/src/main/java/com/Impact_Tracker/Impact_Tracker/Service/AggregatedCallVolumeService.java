package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.AggregatedCallVolume;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.CallVolume;
import com.Impact_Tracker.Impact_Tracker.Repo.AggregatedCallVolumeRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallVolumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AggregatedCallVolumeService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CallVolumeRepository callVolumeRepository;

    @Autowired
    private AggregatedCallVolumeRepository aggregatedRepo;


    @Transactional

    // @Scheduled(cron = "0 0/1 * * * ?") // runs every minute for testing; adjust for production
    public void aggregateCallVolumes() {
        System.out.println("Aggregation started at: " + LocalDateTime.now());
        
        // Get all businesses once
        List<Business> allBusinesses = businessRepository.findAll();
        
        // Get unique business types and sizes (convert to lower-case for consistency)
        Set<String> businessTypes = allBusinesses.stream()
            .filter(b -> b.getBusinessType() != null)
            .map(b -> b.getBusinessType().toLowerCase())
            .collect(Collectors.toSet());
            
        Set<String> businessSizes = allBusinesses.stream()
            .filter(b -> b.getBusinessSize() != null)
            .map(b -> b.getBusinessSize().toLowerCase())
            .collect(Collectors.toSet());
        
        // Loop over each combination of type and size
        for (String targetType : businessTypes) {
            for (String targetSize : businessSizes) {
                System.out.println("Aggregating data for Business Type: " + targetType + ", Size: " + targetSize);
                List<Business> filteredBusinesses = allBusinesses.stream()
                    .filter(b -> b.getBusinessType() != null && b.getBusinessType().equalsIgnoreCase(targetType)
                              && b.getBusinessSize() != null && b.getBusinessSize().equalsIgnoreCase(targetSize))
                    .collect(Collectors.toList());
                
                System.out.println("Total businesses found: " + filteredBusinesses.size());
                if (filteredBusinesses.isEmpty()) {
                    continue;
                }
                
                // Get call volume records for these filtered businesses
              
                // Get call volume records for these filtered businesses
List<CallVolume> callVolumes = callVolumeRepository.findAll().stream()
.filter(cv -> filteredBusinesses.stream()
    .anyMatch(b -> b.getBusinessId().equals(cv.getBusiness().getBusinessId())))
.collect(Collectors.toList());

                System.out.println("Total call volume records fetched: " + callVolumes.size());
                if (callVolumes.isEmpty()) {
                    System.out.println("No call volume data found for the filtered businesses.");
                    continue;
                }
                
                // Aggregate call volume data monthwise
                Map<String, AggregationHelper> monthAggregation = new HashMap<>();
                for (CallVolume cv : callVolumes) {
                    List<String> months = cv.getMonths();
                    List<Integer> answered = cv.getAnswered();
                    List<Integer> missed = cv.getMissed();
                    List<Integer> voicemail = cv.getVoicemail();
                    
                    // Assuming each list is of equal length and the i-th element corresponds to the same month
                    for (int i = 0; i < months.size(); i++) {
                        String month = months.get(i);
                        AggregationHelper helper = monthAggregation.getOrDefault(month, new AggregationHelper());
                        helper.totalAnswered += answered.get(i);
                        helper.totalMissed += missed.get(i);
                        helper.totalVoicemail += voicemail.get(i);
                        helper.count++;
                        monthAggregation.put(month, helper);
                    }
                }
                
                // Save aggregated results for each month
                for (Map.Entry<String, AggregationHelper> entry : monthAggregation.entrySet()) {
                    String month = entry.getKey();
                    AggregationHelper helper = entry.getValue();
                    
                    AggregatedCallVolume aggregated = new AggregatedCallVolume();
                    aggregated.setBusinessType(targetType);
                    aggregated.setBusinessSize(targetSize);
                    aggregated.setMonth(month);
                    aggregated.setAvgAnswered(helper.totalAnswered / (double) helper.count);
                    aggregated.setAvgMissed(helper.totalMissed / (double) helper.count);
                    aggregated.setAvgVoicemail(helper.totalVoicemail / (double) helper.count);
                    aggregated.setAggregatedAt(LocalDateTime.now());
                    
                    aggregatedRepo.save(aggregated);
                    System.out.println("Saved Aggregated data for month " + month + ": AvgAnswered=" + aggregated.getAvgAnswered() 
                        + ", AvgMissed=" + aggregated.getAvgMissed() 
                        + ", AvgVoicemail=" + aggregated.getAvgVoicemail());
                }
            }
        }
        System.out.println("Aggregation completed at: " + LocalDateTime.now());
    }
    
    // Helper class remains the same:
    private static class AggregationHelper {
        int totalAnswered = 0;
        int totalMissed = 0;
        int totalVoicemail = 0;
        int count = 0;
    }
    
}
