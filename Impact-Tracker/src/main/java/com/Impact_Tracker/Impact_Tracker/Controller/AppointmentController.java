package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.DTO.AppointmentDto;
import com.Impact_Tracker.Impact_Tracker.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // CREATE
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto created = appointmentService.createAppointment(appointmentDto);
        return ResponseEntity.ok(created);
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable("id") Long appointmentId) {
        AppointmentDto dto = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(dto);
    }

    // GET all
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> list = appointmentService.getAllAppointments();
        return ResponseEntity.ok(list);
    }

     @GetMapping("/business/{businessId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByBusinessId(@PathVariable Long businessId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByBusinessId(businessId);
        return ResponseEntity.ok(appointments);
    }

     @GetMapping("/business/{businessId}/date/{date}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByBusinessIdAndDate(
            @PathVariable Long businessId,
            @PathVariable String date
    ) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByBusinessIdAndDate(businessId, date);
        return ResponseEntity.ok(appointments);
    }

    

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable("id") Long appointmentId,
                                                            @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto updated = appointmentService.updateAppointment(appointmentId, appointmentDto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
