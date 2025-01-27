package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;
import com.Impact_Tracker.Impact_Tracker.Repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AppointmentConflictService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Checks if there is any time conflict for a given business and date,
     * ignoring (optionally) the same appointment ID if we are updating.
     *
     * @param businessId   the business ID
     * @param appointmentDate  the date (e.g., "2025-01-28")
     * @param startTimeStr the requested start time (e.g., "09:00")
     * @param endTimeStr   the requested end time (e.g., "10:00")
     * @param currentApptId (nullable) if updating an appointment, pass that ID to exclude it from the check
     * @throws RuntimeException if a conflict is found
     */
    public void checkForConflict(Long businessId,
                                 String appointmentDate,
                                 String startTimeStr,
                                 String endTimeStr,
                                 Long currentApptId) {

        // If times are stored as strings, parse them to LocalTime for comparison
        // (assuming your strings are in "HH:mm" format)
        LocalTime requestedStart = parseToLocalTime(startTimeStr);
        LocalTime requestedEnd = parseToLocalTime(endTimeStr);

        // 1) Get all appointments for this business & date
        List<Appointment> sameDayAppointments =
                appointmentRepository.findByBusinessIdAndDate(businessId, appointmentDate);

        // 2) Loop over them and see if time range overlaps
        for (Appointment existing : sameDayAppointments) {
            // Skip if it's the same appointment being updated
            if (currentApptId != null && existing.getAppointmentId().equals(currentApptId)) {
                continue; 
            }

            // Parse existing times
            LocalTime existingStart = parseToLocalTime(existing.getAppointmentStartTime());
            LocalTime existingEnd = parseToLocalTime(existing.getAppointmentEndTime());

            // Basic overlap check:
            // Overlap if (requestedStart < existingEnd) AND (requestedEnd > existingStart)
            if (existingStart != null && existingEnd != null &&
                requestedStart != null && requestedEnd != null) {
                
                boolean overlaps = requestedStart.isBefore(existingEnd) 
                                   && requestedEnd.isAfter(existingStart);
                if (overlaps) {
                    throw new RuntimeException(
                            "Time conflict detected! " +
                            "Business already has an appointment from " + existingStart + " to " + existingEnd +
                            " on " + appointmentDate + ". Please reschedule."
                    );
                }
            }
        }
    }

    // Helper method to parse "HH:mm" or return null if empty
    private LocalTime parseToLocalTime(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            // You can throw or log error
            throw new RuntimeException("Invalid time format: " + timeStr);
        }
    }
}
