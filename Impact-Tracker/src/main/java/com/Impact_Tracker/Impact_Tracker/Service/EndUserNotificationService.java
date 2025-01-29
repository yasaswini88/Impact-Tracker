package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.*;
import com.Impact_Tracker.Impact_Tracker.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.Impact_Tracker.Impact_Tracker.Entity.Appointment;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EndUserNotificationService {

    @Autowired
    private BusinessWeatherNotificationRepository bwnRepository;

    @Autowired
    private WeatherForecastRepository wfRepository;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EndUserConfirmationRepository endUserConfirmationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TwilioStudioService twilioStudioService;

    @Autowired
private AppointmentRepository appointmentRepository;


    

    /**
     * Runs every minute: (0 * * * * *) 
     *  - second=0
     *  - minute=EVERY
     *  - hour=EVERY
     *  - day-of-month=EVERY
     *  - month=EVERY
     *  - day-of-week=EVERY
     */
    // @Scheduled(cron = "0 0/5 * * * ?")
//     @Scheduled(cron="0 1 1 * * *")
//     public void sendNotificationsToEndUsers() {
//         // 1) Find notifications with business_confirmed='Y' and status='RESPONDED'
//         List<BusinessWeatherNotification> pendingNotifications =
//                 bwnRepository.findAll().stream()
//                         .filter(bwn -> "Y".equalsIgnoreCase(bwn.getBusinessConfirmed()))
//                         .filter(bwn -> "RESPONDED".equalsIgnoreCase(bwn.getStatus()))
//                         .toList();

//         for (BusinessWeatherNotification bwn : pendingNotifications) {
//             try {
//                 // 2) Mark them as 'Handled' so we don't process again
//                 bwn.setStatus("Handled");
//                 bwn.setUpdatedAt(LocalDateTime.now());
//                 bwnRepository.save(bwn);

//                 // 3) Look up the WeatherForecast
//                 if (bwn.getForecastId() != null) {
//                     WeatherForecast wf = wfRepository.findById(bwn.getForecastId())
//                             .orElse(null);
//                     if (wf != null) {
//                         // 4) Grab the forecast_date
//                         //    e.g. "2025-01-25" from wf.getForecastDate()
//                         String forecastDate = wf.getForecastDate().toString();  
//                         //    or LocalDate forecastDateObj = wf.getForecastDate();

//                         // 5) Find appointments that match (same businessId, same date)
//                         List<Appointment> appointments =
//                                 appointmentRepository.findAll().stream()
//                                         .filter(a -> a.getBusiness().getBusinessId().equals(bwn.getBusinessId()))
//                                         .filter(a -> forecastDate.equals(a.getAppointmentDate()))
//                                         .toList();

//                         // 6) For each appointment, find customer, send email
//                         for (Appointment appt : appointments) {
//                             Long customerId = appt.getCustomer().getCustomerId();
//                             Customer customer = customerRepository.findById(customerId).orElse(null);
//                             if (customer != null) {
//                                 String customerEmail = customer.getEmail();

//                                 // Build the subject
//                                 String subject = "Weather Impact on Your Appointment";
                                
//                                 // Build the body HTML
//                                 // Suppose we create new endpoints: 
//                                 //   /api/v1/end-user/appointment-yes?appointmentId=...&customerId=... 
//                                 //   /api/v1/end-user/appointment-no?appointmentId=...&customerId=...
//                                 String bodyHtml = buildEmailHtml(appt, wf);

//                                 // 7) Send an email
//                                 emailService.sendEmailWithLinks(customerEmail, subject, bodyHtml);

//                                 // 8) Create a new row in EndUserConfirmation
//                                 EndUserConfirmation euc = new EndUserConfirmation();
//                                 euc.setAppointmentId(appt.getAppointmentId());
//                                 euc.setBusinessId(bwn.getBusinessId());
//                                 euc.setCustomerId(customer.getCustomerId());
//                                 euc.setCustomerResponse("PENDING"); // They haven't clicked yet
//                                 euc.setStatus("SENT");
//                                 euc.setCreatedAt(LocalDateTime.now());
//                                 euc.setUpdatedAt(LocalDateTime.now());

//                                 endUserConfirmationRepository.save(euc);
//                             }
//                         }
//                     }
//                 }

//             } catch (Exception e) {
//                 e.printStackTrace();
//                 // log or handle error gracefully
//             }
//         }
//     }

// private String buildEmailHtml(Appointment appointment, WeatherForecast wf) {
//     Long appointmentId = appointment.getAppointmentId();
//     Long businessId = appointment.getBusiness().getBusinessId();

//     // For example, your React UI might run on "http://localhost:3000"
//     // or maybe an AWS EC2 address. We'll assume localhost for dev.
//     String reactCalendarUrl = "http://localhost:3000/business-calendar/" 
//             + businessId 
//             + "?appointmentId=" 
//             + appointmentId;

//     // Optionally, you can include weather info or any other instructions
//     String forecastCondition = (wf.getWeatherCondition() == null) ? "N/A" : wf.getWeatherCondition();

//     // Simple HTML body with one link
//     return "<html><body>"
//             + "<h3>Weather Update</h3>"
//             + "<p>Your appointment on " + appointment.getAppointmentDate() 
//             + " may be affected by weather (" + forecastCondition + ").</p>"
//             + "<p>Please click the link below if you'd like to view the calendar and reschedule:</p>"
//             + "<p><a href='" + reactCalendarUrl + "'>Open Calendar</a></p>"
//             + "<p>Thank you!</p>"
//             + "</body></html>";
// }

// @Scheduled(cron="0 1 1 * * *")
 @Scheduled(cron = "0 0/5 * * * ?")
    public void sendNotificationsToEndUsers() {
        // 1) Find notifications that are "Y" and "RESPONDED"
        List<BusinessWeatherNotification> pendingNotifications =
                bwnRepository.findAll().stream()
                        .filter(bwn -> "Y".equalsIgnoreCase(bwn.getBusinessConfirmed()))
                        .filter(bwn -> "RESPONDED".equalsIgnoreCase(bwn.getStatus()))
                        .toList();

        for (BusinessWeatherNotification bwn : pendingNotifications) {
            try {
                // 2) Mark them as 'Handled' so we don't process them again
                bwn.setStatus("Handled");
                bwn.setUpdatedAt(LocalDateTime.now());
                bwnRepository.save(bwn);

                // 3) Lookup the WeatherForecast
                if (bwn.getForecastId() != null) {
                    WeatherForecast wf = wfRepository.findById(bwn.getForecastId()).orElse(null);
                    if (wf != null) {
                        // 4) Convert the forecast date to a string or LocalDate
                        LocalDate forecastDate = wf.getForecastDate(); 
                        // e.g. 2025-01-27

                        // 5) Find appointments for that business on that date
                        List<Appointment> appointments = appointmentRepository.findAll().stream()
                                .filter(a -> a.getBusiness().getBusinessId().equals(bwn.getBusinessId()))
                                .filter(a -> a.getAppointmentDate() != null)
                                .filter(a -> {
                                    // Appointment date is a string, so parse to LocalDate
                                    LocalDate apptDate = LocalDate.parse(a.getAppointmentDate());
                                    return forecastDate.isEqual(apptDate);
                                })
                                .toList();

                        // 6) For each appointment, call the customer with 3 new date/time options
                        for (Appointment appt : appointments) {
                            Long customerId = appt.getCustomer().getCustomerId();
                            Customer customer = customerRepository.findById(customerId).orElse(null);
                            if (customer != null) {
                                // We have the original date/time in the Appointment
                                LocalDate originalDate = LocalDate.parse(appt.getAppointmentDate()); 
                                String startTime = appt.getAppointmentStartTime(); // e.g. "09:00"
                                String endTime   = appt.getAppointmentEndTime();   // e.g. "10:00"

                                // Build 3 reschedule options
                                LocalDate option1Date = originalDate.plusDays(1);
                                LocalDate option2Date = originalDate.plusDays(2);
                                LocalDate option3Date = originalDate.plusDays(3);

                                String option1 = option1Date + " from " + startTime + " to " + endTime;
                                String option2 = option2Date + " from " + startTime + " to " + endTime;
                                String option3 = option3Date + " from " + startTime + " to " + endTime;

                                // 7) Place the phone call
                                String phoneNumber = customer.getPhNo(); // or getPhoneNumber()
                                if (phoneNumber == null || phoneNumber.isBlank()) {
                                    System.out.println("Customer has no phone number, skipping call...");
                                    continue;
                                }

                                System.out.println("Calling customer " + phoneNumber 
                                        + " with 3 options: \n1) " + option1 
                                        + "\n2) " + option2 
                                        + "\n3) " + option3);

                                String result = twilioStudioService.callCustomerForReschedule(
                                        phoneNumber,
                                        appt.getAppointmentId(),
                                        appt.getBusiness().getBusinessId(),
                                        option1,
                                        option2,
                                        option3
                                );
                                System.out.println("Customer call result: " + result);

                                // 8) Create a new row in EndUserConfirmation
                                EndUserConfirmation euc = new EndUserConfirmation();
                                euc.setAppointmentId(appt.getAppointmentId());
                                euc.setBusinessId(bwn.getBusinessId());
                                euc.setCustomerId(customer.getCustomerId());
                                euc.setCustomerResponse("PENDING"); 
                                euc.setStatus("SENT");
                                euc.setCreatedAt(LocalDateTime.now());
                                euc.setUpdatedAt(LocalDateTime.now());

                                endUserConfirmationRepository.save(euc);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                
            }
        }
    }

}
