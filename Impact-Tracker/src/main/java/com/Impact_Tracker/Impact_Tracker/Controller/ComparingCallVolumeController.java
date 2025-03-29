// File: com/Impact_Tracker/Impact_Tracker/Controller/ComparingCallVolumeController.java
package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.ComparingCallVolumeResponseDto;
import com.Impact_Tracker.Impact_Tracker.Service.ComparingCallVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comparing-call-volume")
public class ComparingCallVolumeController {

    @Autowired
    private ComparingCallVolumeService comparingService;

    @GetMapping("/{businessId}")
    public ResponseEntity<ComparingCallVolumeResponseDto> getComparingCallVolume(@PathVariable Long businessId) {
        ComparingCallVolumeResponseDto response = comparingService.getComparingCallVolumeData(businessId);
        return ResponseEntity.ok(response);
    }
}
