package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Config.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Twilio helper classes
import com.twilio.http.TwilioRestClient;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@Service
public class TwilioStudioService {

    private final TwilioConfig twilioConfig;
    private static final String FLOW_SID = "FWa0b4b625f359513cd29d6e5f38e9345e";


    private static final String CUSTOMER_RESCHEDULE_FLOW_SID = "FW7f2cd49edc6398a24193d6d6950671d6";

    @Autowired
    public TwilioStudioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

public String createStudioExecution(String toPhoneNumber,
                                        String rescheduleText,
                                        Long forecastId,
                                        Long businessId)
                                        // String flowSid) {

{
        // 1) Build the TwilioRestClient
        TwilioRestClient client = new TwilioRestClient.Builder(
            twilioConfig.getAccountSid(),
            twilioConfig.getAuthToken()
        ).build();

        // 2) Create a Request with the correct domain and path
        Request request = new Request(
            HttpMethod.POST,
            "studio",   
            "/v2/Flows/" + FLOW_SID + "/Executions"
        );

        // 3) Build JSON with text + IDs
        String paramJson = "{"
          + "\"RescheduleText\":\"" + rescheduleText + "\","
          + "\"forecastId\":\"" + forecastId + "\","
          + "\"businessId\":\"" + businessId + "\""
          + "}";

        // Add form params
        request.addPostParam("To", toPhoneNumber);
        request.addPostParam("From", twilioConfig.getFromPhoneNumber());
        request.addPostParam("Parameters", paramJson);

        // 4) Make the HTTP request
        Response response = client.request(request);
        if (response == null) {
            return "No response from Twilio.";
        }

        // 5) Check the result
        int status = response.getStatusCode();
        String content = response.getContent();
        if (status >= 200 && status < 300) {
            return "Call started successfully!\n" + content;
        }

//         else if (status == 409) {
//     System.out.println("Twilio 409 conflict in createStudioExecution. Sleeping 60s, then retrying...");
//     try {
//         Thread.sleep(60_000);
//     } catch (InterruptedException e) {
//         e.printStackTrace();
//     }
//     // Retry
//     return this.createStudioExecution(toPhoneNumber, rescheduleText, forecastId, businessId);
// }

      
        else {
            return "Error: " + status + " - " + content;
        }
    }


  public String callCustomerForReschedule(
            String toPhoneNumber,
            Long appointmentId,
            Long businessId,      
            String option1Text,    
            String option2Text,   
            String option3Text    
    ) {
        // 1) Build TwilioRestClient
        TwilioRestClient client = new TwilioRestClient.Builder(
                twilioConfig.getAccountSid(),
                twilioConfig.getAuthToken()
        ).build();

        // 2) Create the request for your "customer" Flow
        Request request = new Request(
                HttpMethod.POST,
                "studio",
                "/v2/Flows/" + CUSTOMER_RESCHEDULE_FLOW_SID + "/Executions"
        );

        
        String paramJson = "{"
            + "\"appointmentId\":\"" + appointmentId + "\","
            + "\"businessId\":\"" + businessId + "\","
            + "\"option1\":\"" + option1Text + "\","
            + "\"option2\":\"" + option2Text + "\","
            + "\"option3\":\"" + option3Text + "\""
            + "}";

        // 4) Add the post params (just like before)
        request.addPostParam("To", toPhoneNumber);
        request.addPostParam("From", twilioConfig.getFromPhoneNumber());
        request.addPostParam("Parameters", paramJson);

        // 5) Execute
        Response response = client.request(request);
        if (response == null) {
            return "No response from Twilio.";
        }

        // 6) Check status
        int status = response.getStatusCode();
        String content = response.getContent();
        if (status >= 200 && status < 300) {
            return "Call to customer started successfully!\n" + content;
        } 
    //     else if (status == 409) {
    //     // This means Twilio says "already active" (code 20409).
    //     // We do NOT want to Stop=active, so either:
    //     // 1) Sleep & retry, or
    //     // 2) Return an error & let a scheduled job try again later.

    //     System.out.println("Twilio 409 conflict. Old call is still active. We'll wait 60s then try again...");

    //     try {
    //         // WAIT for 1 minute
    //         Thread.sleep(60_000);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }

    //     // Then call this method again or return a message so
    //     // a scheduled job can re‐attempt in the next cycle.
    //     // Example of a quick re‐attempt:
    //     return this.callCustomerForReschedule(
    //         toPhoneNumber,
    //         appointmentId,
    //         businessId,
    //         option1Text,
    //         option2Text,
    //         option3Text
    //     );
    // } 
        else {
            return "Error: " + status + " - " + content;
        }
    }




}