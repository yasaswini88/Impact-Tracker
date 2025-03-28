package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.CallVolumeDto;
import com.Impact_Tracker.Impact_Tracker.Service.CallVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/call-volumes")
public class CallVolumeController {

    @Autowired
    private CallVolumeService callVolumeService;

    // Create new call volume record
    @PostMapping
    public ResponseEntity<CallVolumeDto> createCallVolume(@RequestBody CallVolumeDto dto) {
        CallVolumeDto created = callVolumeService.createCallVolume(dto);
        return ResponseEntity.ok(created);
    }

    // Get call volumes for a specific business
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<CallVolumeDto>> getCallVolumesByBusiness(@PathVariable Long businessId) {
        List<CallVolumeDto> list = callVolumeService.getCallVolumesByBusinessId(businessId);
        return ResponseEntity.ok(list);
    }


}
