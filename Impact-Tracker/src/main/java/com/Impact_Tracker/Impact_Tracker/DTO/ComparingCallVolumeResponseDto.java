// File: com/Impact_Tracker/Impact_Tracker/DTO/ComparingCallVolumeResponseDto.java
package com.Impact_Tracker.Impact_Tracker.DTO;

import java.util.List;

public class ComparingCallVolumeResponseDto {
    private CallVolumeDto businessCallVolume;
    private List<AggregatedCallVolumeDto> aggregatedCallVolumes;

    // Getters and setters
    public CallVolumeDto getBusinessCallVolume() {
        return businessCallVolume;
    }
    public void setBusinessCallVolume(CallVolumeDto businessCallVolume) {
        this.businessCallVolume = businessCallVolume;
    }
    public List<AggregatedCallVolumeDto> getAggregatedCallVolumes() {
        return aggregatedCallVolumes;
    }
    public void setAggregatedCallVolumes(List<AggregatedCallVolumeDto> aggregatedCallVolumes) {
        this.aggregatedCallVolumes = aggregatedCallVolumes;
    }
}
