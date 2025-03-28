package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.CallVolumeDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.CallVolume;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.CallVolumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CallVolumeService {

    @Autowired
    private CallVolumeRepository callVolumeRepository;

    @Autowired
    private BusinessRepository businessRepository;

    // Create a new CallVolume record
    public CallVolumeDto createCallVolume(CallVolumeDto dto) {
        CallVolume callVolume = mapToEntity(dto);
        // Fetch the associated business
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + dto.getBusinessId()));
        callVolume.setBusiness(business);

        CallVolume saved = callVolumeRepository.save(callVolume);
        return mapToDto(saved);
    }

    // Get CallVolume records for a business
    public List<CallVolumeDto> getCallVolumesByBusinessId(Long businessId) {
        List<CallVolume> list = callVolumeRepository.findByBusiness_BusinessId(businessId);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Update, Delete methods can be added similarly

    private CallVolumeDto mapToDto(CallVolume cv) {
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

    private CallVolume mapToEntity(CallVolumeDto dto) {
        CallVolume cv = new CallVolume();
        cv.setMonths(dto.getMonths());
        cv.setAnswered(dto.getAnswered());
        cv.setMissed(dto.getMissed());
        cv.setVoicemail(dto.getVoicemail());
        // dateCreated is automatically set on persist
        return cv;
    }
}
