package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessLoginLogsDto;
import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.BusinessLoginLogs;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessLoginLogsRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessLoginLogsService {

    @Autowired
    private BusinessLoginLogsRepository loginLogsRepository;

    @Autowired
    private BusinessRepository businessRepository;

    public BusinessLoginLogsDto createLoginLog(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));

        BusinessLoginLogs loginLog = new BusinessLoginLogs();
        loginLog.setBusiness(business);
        loginLog.setLoginTime(LocalDateTime.now());
        BusinessLoginLogs savedLog = loginLogsRepository.save(loginLog);
        return mapToDto(savedLog);
    }

    public List<BusinessLoginLogsDto> getLogsByBusinessId(Long businessId) {
        List<BusinessLoginLogs> logs = loginLogsRepository.findByBusiness_BusinessId(businessId);
        return logs.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Additional methods such as getAllLogs() can be added if needed

    private BusinessLoginLogsDto mapToDto(BusinessLoginLogs log) {
        BusinessLoginLogsDto dto = new BusinessLoginLogsDto();
        dto.setLogId(log.getLogId());
        dto.setBusinessId(log.getBusiness().getBusinessId());
        dto.setLoginTime(log.getLoginTime());
        return dto;
    }
}
