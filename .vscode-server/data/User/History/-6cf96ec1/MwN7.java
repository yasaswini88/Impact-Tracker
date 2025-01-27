package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.*;
import com.Impact_Tracker.Impact_Tracker.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EndUserNotificationService {

    @Autowired
    private BusinessWeatherNotificationRepository bwnRepository;

    @Autowired
    private WeatherForecastRepository wfRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EndUserConfirmationRepository endUserConfirmationRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Runs every minute: (0 * * * * *) 
     *  - second=0
     *  - minute=EVERY
     *  - hour=EVERY
     *  - day-of-month=EVERY
     *  - month=EVERY
     *  - day-of-week=EVERY
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void sendNotificationsToEndUsers() {
        // 1) Find notifications with business_confirmed='Y' and status='RESPONDED'
        List<BusinessWeatherNotification> pendingNotifications =
                bwnRepository.findAll().stream()
                        .filter(bwn -> "Y".equalsIgnoreCase(bwn.getBusinessConfirmed()))
                        .filter(bwn -> "RESPONDED".equalsIgnoreCase(bwn.getStatus()))
                        .toList();

        for (BusinessWeatherNotification bwn : pendingNotifications) {
            try {
                // 2) Mark them as 'Handled' so we don't process again
                bwn.setStatus("Handled");
                bwn.setUpdatedAt(LocalDateTime.now());
                bwnRepository.save(bwn);

                // 3) Look up the WeatherForecast
                if (bwn.getForecastId() != null) {
                    WeatherForecast wf = wfRepository.findById(bwn.getForecastId())
                            .orElse(null);
                    if (wf != null) {
                        // 4) Grab the forecast_date
                        //    e.g. "2025-01-25" from wf.getForecastDate()
                        String forecastDate = wf.getForecastDate().toString();  
                        //    or LocalDate forecastDateObj = wf.getForecastDate();

                        // 5) Find appointments that match (same businessId, same date)
                        List<Appointment> appointments =
                                appointmentRepository.findAll().stream()
                                        .filter(a -> a.getBusiness().getBusinessId().equals(bwn.getBusinessId()))
                                        .filter(a -> forecastDate.equals(a.getAppointmentDate()))
                                        .toList();

                        // 6) For each appointment, find customer, send email
                        for (Appointment appt : appointments) {
                            Long customerId = appt.getCustomer().getCustomerId();
                            Customer customer = customerRepository.findById(customerId).orElse(null);
                            if (customer != null) {
                                String customerEmail = customer.getEmail();

                                // Build the subject
                                String subject = "Weather Impact on Your Appointment";
                                
                                // Build the body HTML
                                // Suppose we create new endpoints: 
                                //   /api/v1/end-user/appointment-yes?appointmentId=...&customerId=... 
                                //   /api/v1/end-user/appointment-no?appointmentId=...&customerId=...
                                String bodyHtml = buildEmailHtml(appt, wf);

                                // 7) Send an email
                                emailService.sendEmailWithLinks(customerEmail, subject, bodyHtml);

                                // 8) Create a new row in EndUserConfirmation
                                EndUserConfirmation euc = new EndUserConfirmation();
                                euc.setAppointmentId(appt.getAppointmentId());
                                euc.setBusinessId(bwn.getBusinessId());
                                euc.setCustomerId(customer.getCustomerId());
                                euc.setCustomerResponse("PENDING"); // They haven't clicked yet
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
                // log or handle error gracefully
            }
        }
    }

    private String buildEmailHtml(Appointment appointment, WeatherForecast wf) {
        // Build your HTML with links for YES / NO
        // e.g.: http://<server>/api/v1/end-user/appointment-yes?appointmentId=xxx&customerId=yyy
        //        &businessId=zzz
        Long appointmentId = appointment.getAppointmentId();
        Long customerId = appointment.getCustomer().getCustomerId();
        Long businessId = appointment.getBusiness().getBusinessId();

        String forecastCondition = (wf.getWeatherCondition() == null) ? "N/A" : wf.getWeatherCondition();

        return "<html><body>"
                + "<h3>Weather Update</h3>"
                + "<p>Your appointment on " + appointment.getAppointmentDate() + " may be affected by weather ("
                + forecastCondition + "). Would you like to reschedule?</p>"
                + "<p>"
                + "<a href='http://3.95.195.91:8080/api/v1/end-user/appointment-yes?appointmentId=" + appointmentId
                   + "&customerId=" + customerId
                   + "&businessId=" + businessId
                   + "'>YES, reschedule</a><br>"
                + "<a href='http://3.95.195.91:8080/api/v1/end-user/appointment-no?appointmentId=" + appointmentId
                   + "&customerId=" + customerId
                   + "&businessId=" + businessId
                   + "'>NO, keep my appointment</a>"
                + "</p>"
                + "</body></html>";
    }
}
