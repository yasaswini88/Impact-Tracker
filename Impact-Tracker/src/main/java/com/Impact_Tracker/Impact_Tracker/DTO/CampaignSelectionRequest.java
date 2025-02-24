package com.Impact_Tracker.Impact_Tracker.DTO;

import java.util.List;

public class CampaignSelectionRequest {
    
    private Long businessId;
    // The new approach:
    private List<CallCampaignSelectionDto> selections; 

    public CampaignSelectionRequest() {
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public List<CallCampaignSelectionDto> getSelections() {
        return selections;
    }

    public void setSelections(List<CallCampaignSelectionDto> selections) {
        this.selections = selections;
    }
}
