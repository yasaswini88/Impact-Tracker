package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.*;
import com.Impact_Tracker.Impact_Tracker.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Example: Two separate scheduled methods
 *  1) sendEmailNotificationsToEndUsers()  -- sends emails, sets status to EMAIL_HANDLED
 *  2) callEndUsersForReschedule()         -- calls, then sets final status to Handled
 */
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

    // ------------------------------------------------------------------------
    // 1) SCHEDULED METHOD: SEND EMAILS
    // ------------------------------------------------------------------------
    /**
     * Runs once a day at 1:01 AM (example).
     *  - Looks for businessConfirmed='Y' and status='RESPONDED' (meaning the business said "Yes")
     *  - Sends emails to customers, sets BWN status to EMAIL_HANDLED (not fully Handled yet).
     */
    // @Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(cron = "0 1 1 * * *")
    public void sendEmailNotificationsToEndUsers() {
        // 1) Find BWN records with business_confirmed='Y' and status='RESPONDED'
        List<BusinessWeatherNotification> pendingNotifications =
                bwnRepository.findAll().stream()
                        .filter(bwn -> "Y".equalsIgnoreCase(bwn.getBusinessConfirmed()))
                        .filter(bwn -> "RESPONDED".equalsIgnoreCase(bwn.getStatus()))
                        .toList();

        for (BusinessWeatherNotification bwn : pendingNotifications) {
            try {
                // 2) Find the WeatherForecast
                if (bwn.getForecastId() != null) {
                    WeatherForecast wf = wfRepository.findById(bwn.getForecastId()).orElse(null);
                    if (wf != null) {
                        // 3) Determine the forecast date
                        LocalDate forecastDate = wf.getForecastDate();

                        // 4) Find all relevant appointments
                        List<Appointment> appointments =
                                appointmentRepository.findAll().stream()
                                        .filter(a -> a.getBusiness().getBusinessId().equals(bwn.getBusinessId()))
                                        .filter(a -> a.getAppointmentDate() != null)
                                        .filter(a -> {
                                            LocalDate apptDate = LocalDate.parse(a.getAppointmentDate());
                                            return forecastDate.isEqual(apptDate);
                                        })
                                        .toList();

                        // 5) For each appointment, send an email
                        for (Appointment appt : appointments) {
                            Customer customer = appt.getCustomer();
                            if (customer != null) {
                                String customerEmail = customer.getEmail();
                                if (customerEmail != null && !customerEmail.isBlank()) {
                                    // Build subject & HTML
                                    String subject = "Weather Impact on Your Appointment";
                                    String bodyHtml = buildEmailHtml(appt, wf);

                                    // Send email
                                    emailService.sendEmailWithLinks(customerEmail, subject, bodyHtml);

                                    // Create or update an EndUserConfirmation (status=SENT)
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
                }

                // 6) After sending emails, set BWN status to EMAIL_HANDLED (not "Handled" yet)
                bwn.setStatus("EMAIL_HANDLED");
                bwn.setUpdatedAt(LocalDateTime.now());
                bwnRepository.save(bwn);

            } catch (Exception e) {
                e.printStackTrace();
                // log error as needed
            }
        }
    }

    // ------------------------------------------------------------------------
    // 2) SCHEDULED METHOD: CALL END USERS
    // ------------------------------------------------------------------------
    /**
     * Runs every 5 minutes (example).
     *  - Looks for businessConfirmed='Y' and status='EMAIL_HANDLED'
     *  - Makes the Twilio phone calls, then sets BWN status to 'Handled' finally
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void callEndUsersForReschedule() {
        // 1) Find BWN records with business_confirmed='Y' and status='EMAIL_HANDLED'
        List<BusinessWeatherNotification> pendingNotifications =
                bwnRepository.findAll().stream()
                        .filter(bwn -> "Y".equalsIgnoreCase(bwn.getBusinessConfirmed()))
                        .filter(bwn -> "EMAIL_HANDLED".equalsIgnoreCase(bwn.getStatus()))
                        .toList();

        for (BusinessWeatherNotification bwn : pendingNotifications) {
            try {
                // 2) Find the WeatherForecast
                if (bwn.getForecastId() != null) {
                    WeatherForecast wf = wfRepository.findById(bwn.getForecastId()).orElse(null);
                    if (wf != null) {
                        LocalDate forecastDate = wf.getForecastDate();

                        // 3) Find relevant appointments
                        List<Appointment> appointments = appointmentRepository.findAll().stream()
                                .filter(a -> a.getBusiness().getBusinessId().equals(bwn.getBusinessId()))
                                .filter(a -> a.getAppointmentDate() != null)
                                .filter(a -> {
                                    LocalDate apptDate = LocalDate.parse(a.getAppointmentDate());
                                    return forecastDate.isEqual(apptDate);
                                })
                                .toList();

                        // 4) For each appointment, make Twilio call
                        for (Appointment appt : appointments) {
                            Customer customer = appt.getCustomer();
                            if (customer != null) {
                                String phoneNumber = customer.getPhNo();
                                if (phoneNumber != null && !phoneNumber.isBlank()) {
                                    LocalDate originalDate = LocalDate.parse(appt.getAppointmentDate());
                                    String startTime = appt.getAppointmentStartTime();
                                    String endTime   = appt.getAppointmentEndTime();

                                    // Build 3 new date/time options
                                    String option1 = originalDate.plusDays(1) + " from " + startTime + " to " + endTime;
                                    String option2 = originalDate.plusDays(2) + " from " + startTime + " to " + endTime;
                                    String option3 = originalDate.plusDays(3) + " from " + startTime + " to " + endTime;

                                    System.out.println("Calling " + phoneNumber + " with options:\n 1) " + option1
                                            + "\n 2) " + option2 + "\n 3) " + option3);

                                    String callResult = twilioStudioService.callCustomerForReschedule(
                                            phoneNumber,
                                            appt.getAppointmentId(),
                                            appt.getBusiness().getBusinessId(),
                                            option1, option2, option3
                                    );
                                    System.out.println("Call result: " + callResult);

                                    // We might also create or update EndUserConfirmation if needed
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
                }

                // 5) After phone calls, set BWN status to 'Handled'
                bwn.setStatus("Handled");
                bwn.setUpdatedAt(LocalDateTime.now());
                bwnRepository.save(bwn);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------------------------------------------------------------
    // HELPER METHOD FOR EMAIL CONTENT
    // ------------------------------------------------------------------------
    private String buildEmailHtml(Appointment appointment, WeatherForecast wf) {
        Long appointmentId = appointment.getAppointmentId();
        Long businessId    = appointment.getBusiness().getBusinessId();

        // Example link to your React UI or some page
        String reactCalendarUrl = "http://localhost:3000/business-calendar/"
                + businessId
                + "?appointmentId="
                + appointmentId;

        String forecastCondition = (wf.getWeatherCondition() == null) ? "N/A" : wf.getWeatherCondition();

        return "<html><body>"
                + "<h3>Weather Update</h3>"
                + "<p>Your appointment on " + appointment.getAppointmentDate()
                + " may be affected by weather (" + forecastCondition + ").</p>"
                + "<p>Please click below if you'd like to view the calendar and reschedule:</p>"
                + "<p><a href='" + reactCalendarUrl + "'>Open Calendar</a></p>"
                + "<p>Thank you!</p>"
                + "</body></html>";
    }
}
