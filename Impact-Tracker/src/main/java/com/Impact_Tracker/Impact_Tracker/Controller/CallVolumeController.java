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

    @GetMapping("/business")
public ResponseEntity<List<CallVolumeDto>> getAllCallVolumes() {
    List<CallVolumeDto> callVolumes = callVolumeService.getAllCallVolumes();
    return ResponseEntity.ok(callVolumes);
}


    @PutMapping("/{id}")
    public ResponseEntity<CallVolumeDto> updateCallVolume(@PathVariable Long id, @RequestBody CallVolumeDto dto) {
        // Call the service layer
        CallVolumeDto updated = callVolumeService.updateCallVolume(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCallVolume(@PathVariable Long id) {
        callVolumeService.deleteCallVolume(id);
        return ResponseEntity.noContent().build();
    }

    // Additional methods for other CRUD operations can be added similarly

}
