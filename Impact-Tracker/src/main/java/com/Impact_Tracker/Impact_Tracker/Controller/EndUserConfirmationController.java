package com.Impact_Tracker.Impact_Tracker.Controller;

import com.Impact_Tracker.Impact_Tracker.Entity.EndUserConfirmation;
import com.Impact_Tracker.Impact_Tracker.Repo.EndUserConfirmationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.AppointmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;

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

    /**
     * Endpoint when the customer chooses to "YES, reschedule"
     */
    // @GetMapping("/appointment-yes")
    // public String handleYes(@RequestParam("appointmentId") Long appointmentId,
    //                         @RequestParam("customerId") Long customerId,
    //                         @RequestParam("businessId") Long businessId) {

    //     // Find the EndUserConfirmation record that matches:
    //     //   appointmentId, businessId, customerId, status='SENT' (or 'PENDING')
    //     Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll().stream()
    //             .filter(euc -> euc.getAppointmentId().equals(appointmentId))
    //             .filter(euc -> euc.getBusinessId().equals(businessId))
    //             .filter(euc -> euc.getCustomerId().equals(customerId))
    //             .filter(euc -> "SENT".equalsIgnoreCase(euc.getStatus()))
    //             .findFirst();

    //     if (eucOpt.isEmpty()) {
    //         return "No matching record found or already responded. Possibly invalid link.";
    //     }

    //     EndUserConfirmation euc = eucOpt.get();
    //     euc.setCustomerResponse("YES");
    //     euc.setStatus("RESPONDED");
    //     euc.setDateResponded(LocalDateTime.now());
    //     euc.setUpdatedAt(LocalDateTime.now());
    //     endUserConfirmationRepository.save(euc);

    //     // Additional logic: You might create a new method to reschedule the appointment automatically,
    //     // or let the user pick a new time, etc.
    //     return "Thank you! We will contact you to reschedule your appointment.";
    // }

    // /**
    //  * Endpoint when the customer chooses to "NO, keep the appointment"
    //  */
    // @GetMapping("/appointment-no")
    // public String handleNo(@RequestParam("appointmentId") Long appointmentId,
    //                        @RequestParam("customerId") Long customerId,
    //                        @RequestParam("businessId") Long businessId) {

    //     Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll().stream()
    //             .filter(euc -> euc.getAppointmentId().equals(appointmentId))
    //             .filter(euc -> euc.getBusinessId().equals(businessId))
    //             .filter(euc -> euc.getCustomerId().equals(customerId))
    //             .filter(euc -> "SENT".equalsIgnoreCase(euc.getStatus()))
    //             .findFirst();

    //     if (eucOpt.isEmpty()) {
    //         return "No matching record found or already responded. Possibly invalid link.";
    //     }

    //     EndUserConfirmation euc = eucOpt.get();
    //     euc.setCustomerResponse("NO");
    //     euc.setStatus("RESPONDED");
    //     euc.setDateResponded(LocalDateTime.now());
    //     euc.setUpdatedAt(LocalDateTime.now());
    //     endUserConfirmationRepository.save(euc);

    //     // Possibly keep the appointment as is.
    //     return "Understood. We will keep your appointment as scheduled.";
    // }

@GetMapping("/customerReschedule")
    public String handleCustomerReschedule(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestParam("digit") String digit,
            @RequestParam("businessId") Long businessId
    ) {
        // 1) Find a matching EndUserConfirmation row in PENDING
        Optional<EndUserConfirmation> eucOpt = endUserConfirmationRepository.findAll()
                .stream()
                .filter(euc -> euc.getAppointmentId().equals(appointmentId))
                .filter(euc -> euc.getBusinessId().equals(businessId))
                .filter(euc -> "PENDING".equalsIgnoreCase(euc.getCustomerResponse()))
                .findFirst();

        if (eucOpt.isEmpty()) {
            return "Already responded or invalid appointment.";
        }
        EndUserConfirmation euc = eucOpt.get();

        // 2) Depending on which digit was pressed, reschedule or keep existing
        switch (digit) {
            case "1":
                // e.g., shift appointment by 1 day
                rescheduleAppointment(appointmentId, 1);
                euc.setCustomerResponse("YES_Option1");
                break;

            case "2":
                // shift appointment by 2 days
                rescheduleAppointment(appointmentId, 2);
                euc.setCustomerResponse("YES_Option2");
                break;

            case "3":
                // keep the existing appointment date/time
              rescheduleAppointment(appointmentId, 3);
              euc.setCustomerResponse("YES_Option3");
                break;

            default:
                return "Invalid digit pressed.";
        }

        // 3) Mark the record as responded
        euc.setStatus("RESPONDED");
        euc.setUpdatedAt(LocalDateTime.now());
        euc.setDateResponded(LocalDateTime.now());
        endUserConfirmationRepository.save(euc);

        // 4) Send a simple confirmation string (Twilio will read or store the HTTP response)
        return "Got it! We have processed your choice of option " + digit;
    }

    /**
     * Simple method to demonstrate how you might shift the appointment
     * date. Adjust to your actual business logic for rescheduling.
     */
    private void rescheduleAppointment(Long appointmentId, int daysToShift) {
        Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
        if (apptOpt.isPresent()) {
            Appointment appt = apptOpt.get();

            // If your appointment date is stored as a String (e.g. "2025-01-27")
            // parse it to LocalDate, add the shift, then store it back:
            if (appt.getAppointmentDate() != null) {
                LocalDate oldDate = LocalDate.parse(appt.getAppointmentDate());
                LocalDate newDate = oldDate.plusDays(daysToShift);
                appt.setAppointmentDate(newDate.toString());
            }

            
            // appt.setAppointmentStartTime(...);
            // appt.setAppointmentEndTime(...);
            appt.setAppointmentRescheduled("true");

            appointmentRepository.save(appt);
        }
    }


}
