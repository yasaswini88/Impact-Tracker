// File: com.Impact_Tracker.Impact_Tracker.DTO.CallCampaignSelectionDto.java

package com.Impact_Tracker.Impact_Tracker.DTO;

public class CallCampaignSelectionDto {

    private Long strategyId;
    private String callCampaignVoice;
    private String targetAudience;

    public CallCampaignSelectionDto() {
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
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
