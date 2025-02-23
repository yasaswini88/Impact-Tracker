package com.Impact_Tracker.Impact_Tracker.DTO;

import java.util.List;

public class CampaignSelectionRequest {
    private Long businessId;           // which business
    private List<Long> strategyIds;    // which strategy IDs selected
    private String callCampaignVoice;  // the custom message
    private String targetAudience;     // “POSITIVE”, “NEGATIVE”, “ALL”

    // getters & setters
    public Long getBusinessId() {
        return businessId;
    }
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public List<Long> getStrategyIds() {
        return strategyIds;
    }
    public void setStrategyIds(List<Long> strategyIds) {
        this.strategyIds = strategyIds;
    }

    public String getCallCampaignVoice() {
        return callCampaignVoice;
    }
    public void setCallCampaignVoice(String callCampaignVoice) {
        this.callCampaignVoice = callCampaignVoice;
    }

    public String getTargetAudience() {
        return targetAudience;
    }
    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }
}
