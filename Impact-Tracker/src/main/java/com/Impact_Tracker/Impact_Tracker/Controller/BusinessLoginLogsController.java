package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.BusinessLoginLogsDto;
import com.Impact_Tracker.Impact_Tracker.Service.BusinessLoginLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-login-logs")
public class BusinessLoginLogsController {

    @Autowired
    private BusinessLoginLogsService loginLogsService;

    // Endpoint to create a login log (e.g., when login is successful)
    @PostMapping("/{businessId}")
    public ResponseEntity<BusinessLoginLogsDto> createLoginLog(@PathVariable Long businessId) {
        BusinessLoginLogsDto dto = loginLogsService.createLoginLog(businessId);
        return ResponseEntity.ok(dto);
    }

    // Endpoint to get logs for a specific business
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<BusinessLoginLogsDto>> getLogsByBusiness(@PathVariable Long businessId) {
        List<BusinessLoginLogsDto> logs = loginLogsService.getLogsByBusinessId(businessId);
        return ResponseEntity.ok(logs);
    }
}
