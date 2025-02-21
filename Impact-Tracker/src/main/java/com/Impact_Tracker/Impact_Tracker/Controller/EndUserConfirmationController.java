package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;
import com.Impact_Tracker.Impact_Tracker.Entity.EndUserConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.AppointmentRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.EndUserConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/end-user")
public class EndUserConfirmationController {

    @Autowired
    private EndUserConfirmationRepository endUserConfirmationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/appointment-yes")
    public String handleYes(@RequestParam("appointmentId") Long appointmentId,
                            @RequestParam("customerId") Long customerId,
                            @RequestParam("businessId") Long businessId) {

        // Look for EndUserConfirmation with status='SENT' or 'PENDING'
        Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll().stream()
                .filter(euc -> euc.getAppointmentId().equals(appointmentId))
                .filter(euc -> euc.getBusinessId().equals(businessId))
                .filter(euc -> euc.getCustomerId().equals(customerId))
                .filter(euc -> "SENT".equalsIgnoreCase(euc.getStatus()))
                .findFirst();

        if (eucOpt.isEmpty()) {
            return "No matching record found or already responded. Possibly invalid link.";
        }

        EndUserConfirmation euc = eucOpt.get();
        euc.setCustomerResponse("YES");
        euc.setStatus("RESPONDED");
        euc.setDateResponded(LocalDateTime.now());
        euc.setUpdatedAt(LocalDateTime.now());
        endUserConfirmationRepository.save(euc);

        // Additional logic: e.g., auto-reschedule or let them pick a new slot
        return "Thank you! We will contact you to reschedule your appointment.";
    }

    @GetMapping("/appointment-no")
    public String handleNo(@RequestParam("appointmentId") Long appointmentId,
                           @RequestParam("customerId") Long customerId,
                           @RequestParam("businessId") Long businessId) {

        Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll().stream()
                .filter(euc -> euc.getAppointmentId().equals(appointmentId))
                .filter(euc -> euc.getBusinessId().equals(businessId))
                .filter(euc -> euc.getCustomerId().equals(customerId))
                .filter(euc -> "SENT".equalsIgnoreCase(euc.getStatus()))
                .findFirst();

        if (eucOpt.isEmpty()) {
            return "No matching record found or already responded. Possibly invalid link.";
        }

        EndUserConfirmation euc = eucOpt.get();
        euc.setCustomerResponse("NO");
        euc.setStatus("RESPONDED");
        euc.setDateResponded(LocalDateTime.now());
        euc.setUpdatedAt(LocalDateTime.now());
        endUserConfirmationRepository.save(euc);

        // Possibly keep the appointment as-is
        return "Understood. We will keep your appointment as scheduled.";
    }

    @GetMapping("/customerReschedule")
    public String handleCustomerReschedule(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("digit") String digit,
            @RequestParam("businessId") Long businessId
    ) {
        // 1) Find EndUserConfirmation row in "PENDING"
        Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll().stream()
                .filter(euc -> euc.getAppointmentId().equals(appointmentId))
                .filter(euc -> euc.getBusinessId().equals(businessId))
                .filter(euc -> "PENDING".equalsIgnoreCase(euc.getCustomerResponse()))
                .findFirst();

        if (eucOpt.isEmpty()) {
            return "Already responded or invalid appointment.";
        }

        EndUserConfirmation euc = eucOpt.get();

        // 2) Decide based on digit
        switch (digit) {
            case "1":
                rescheduleAppointment(appointmentId, 1);
                euc.setCustomerResponse("YES_Option1");
                break;
            case "2":
                rescheduleAppointment(appointmentId, 2);
                euc.setCustomerResponse("YES_Option2");
                break;
            case "3":
                // keep existing date/time
                // or shift by 3 days, whichever you prefer
                rescheduleAppointment(appointmentId, 3);
                euc.setCustomerResponse("YES_Option3");
                break;
            default:
                return "Invalid digit pressed.";
        }

        euc.setStatus("RESPONDED");
        euc.setUpdatedAt(LocalDateTime.now());
        euc.setDateResponded(LocalDateTime.now());
        endUserConfirmationRepository.save(euc);

        return "Got it! We have processed your choice of option " + digit;
    }

    /**
     * Example shifting the appointment date by N days
     */
    private void rescheduleAppointment(Long appointmentId, int daysToShift) {
        Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
        if (apptOpt.isPresent()) {
            Appointment appt = apptOpt.get();
            if (appt.getAppointmentDate() != null) {
                LocalDate oldDate = LocalDate.parse(appt.getAppointmentDate());
                LocalDate newDate = oldDate.plusDays(daysToShift);
                appt.setAppointmentDate(newDate.toString());
            }
            appt.setAppointmentRescheduled("true");
            appointmentRepository.save(appt);
        }
    }
}
