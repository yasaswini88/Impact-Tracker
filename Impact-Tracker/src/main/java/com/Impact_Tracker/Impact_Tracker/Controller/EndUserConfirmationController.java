package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.EndUserConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.EndUserConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/end-user")
public class EndUserConfirmationController {

    @Autowired
    private EndUserConfirmationRepository endUserConfirmationRepository;

    /**
     * Endpoint when the customer chooses to "YES, reschedule"
     */
    @GetMapping("/appointment-yes")
    public String handleYes(@RequestParam("appointmentId") Long appointmentId,
                            @RequestParam("customerId") Long customerId,
                            @RequestParam("businessId") Long businessId) {

        // Find the EndUserConfirmation record that matches:
        //   appointmentId, businessId, customerId, status='SENT' (or 'PENDING')
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

        // Additional logic: You might create a new method to reschedule the appointment automatically,
        // or let the user pick a new time, etc.
        return "Thank you! We will contact you to reschedule your appointment.";
    }

    /**
     * Endpoint when the customer chooses to "NO, keep the appointment"
     */
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

        // Possibly keep the appointment as is.
        return "Understood. We will keep your appointment as scheduled.";
    }
}
