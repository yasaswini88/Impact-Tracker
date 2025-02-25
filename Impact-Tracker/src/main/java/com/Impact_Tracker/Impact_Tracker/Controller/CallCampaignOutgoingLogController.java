package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.CallCampaignOutgoingLog;               // ADD THIS
import com.Impact_Tracker.Impact_Tracker.Repo.CallCampaignOutgoingLogRepository;       // ADD THIS
import java.util.List;                                                                 // ADD THIS

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/call-logs")
public class CallCampaignOutgoingLogController {

    @Autowired
    private CallCampaignOutgoingLogRepository logRepository;

    

    @GetMapping
    public List<CallCampaignOutgoingLog> getAllLogs() {
        return logRepository.findAll();
    }

    @GetMapping("/business/{businessId}")
    public List<CallCampaignOutgoingLog> getAllLogsForBusiness(@PathVariable Long businessId) {
        // calls the custom method from the repository
        return logRepository.findByBusinessIdDesc(businessId);
    }
}
