// File: com/Impact_Tracker/Impact_Tracker/Service/ComparingCallVolumeService.java
package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.AggregatedCallVolumeDto;
import com.Impact_Tracker.Impact_Tracker.DTO.CallVolumeDto;
import com.Impact_Tracker.Impact_Tracker.DTO.ComparingCallVolumeResponseDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.CallVolume;
import com.Impact_Tracker.Impact_Tracker.Repo.AggregatedCallVolumeRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallVolumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComparingCallVolumeService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CallVolumeRepository callVolumeRepository;

    @Autowired
    private AggregatedCallVolumeRepository aggregatedRepo;

    public ComparingCallVolumeResponseDto getComparingCallVolumeData(Long businessId) {
        // 1. Fetch the business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));

        // 2. Get the latest call volume record for the specified business
        List<CallVolume> callVolumes = callVolumeRepository.findByBusiness_BusinessId(businessId);
        if (callVolumes.isEmpty()) {
            throw new RuntimeException("No call volume data found for business ID: " + businessId);
        }
        CallVolume latestCallVolume = callVolumes.stream()
                .max(Comparator.comparing(CallVolume::getDateCreated))
                .orElse(callVolumes.get(0));
        CallVolumeDto businessCallVolumeDto = mapToCallVolumeDto(latestCallVolume);

        // 3. Fetch aggregated call volume data for businesses with the same businessType and businessSize
        List<AggregatedCallVolumeDto> aggregatedList = aggregatedRepo.findAll().stream()
                .filter(agg -> agg.getBusinessType() != null && agg.getBusinessType().equalsIgnoreCase(business.getBusinessType())
                        && agg.getBusinessSize() != null && agg.getBusinessSize().equalsIgnoreCase(business.getBusinessSize()))
                .map(this::mapToAggregatedDto)
                .collect(Collectors.toList());

        // 4. Prepare response DTO
        ComparingCallVolumeResponseDto response = new ComparingCallVolumeResponseDto();
        response.setBusinessCallVolume(businessCallVolumeDto);
        response.setAggregatedCallVolumes(aggregatedList);
        return response;
    }

    private CallVolumeDto mapToCallVolumeDto(CallVolume cv) {
        CallVolumeDto dto = new CallVolumeDto();
        dto.setCallVolumeId(cv.getCallVolumeId());
        dto.setBusinessId(cv.getBusiness().getBusinessId());
        dto.setMonths(cv.getMonths());
        dto.setAnswered(cv.getAnswered());
        dto.setMissed(cv.getMissed());
        dto.setVoicemail(cv.getVoicemail());
        dto.setDateCreated(cv.getDateCreated());
        return dto;
    }

    private AggregatedCallVolumeDto mapToAggregatedDto(com.Impact_Tracker.Impact_Tracker.Entity.AggregatedCallVolume agg) {
        AggregatedCallVolumeDto dto = new AggregatedCallVolumeDto();
        dto.setId(agg.getId());
        dto.setBusinessType(agg.getBusinessType());
        dto.setBusinessSize(agg.getBusinessSize());
        dto.setMonth(agg.getMonth());
        dto.setAvgAnswered(agg.getAvgAnswered());
        dto.setAvgMissed(agg.getAvgMissed());
        dto.setAvgVoicemail(agg.getAvgVoicemail());
        dto.setAggregatedAt(agg.getAggregatedAt());
        return dto;
    }
}
