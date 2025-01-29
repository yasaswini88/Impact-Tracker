// package com.Impact_Tracker.Impact_Tracker.Controller;

// import com.Impact_Tracker.Impact_Tracker.Service.TwilioStudioService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/v1/twilio-test")
// public class TwilioTestController {

//     @Autowired
//     private TwilioStudioService twilioStudioService;

//     /**
//      * This endpoint triggers the Twilio Studio Flow call.
//      * @param toPhone The phone number we want to call (e.g. "+17038620152").
//      * @param rescheduleText Optional text that you want Twilio to say. Defaults to "Press 1 for YES..."
//      * @return A string describing success or error from Twilio.
//      */
//     @PostMapping("/call")
//     public String callBusiness(
//         @RequestParam("toPhone") String toPhone,
//         @RequestParam(value="rescheduleText", defaultValue="Press 1 for YES, Press 2 for NO")
//             String rescheduleText
//     ) {
//         // Example usage:
//         // POST http://localhost:8080/api/v1/twilio-test/call?toPhone=+17038620152
//         // Optionally pass: &rescheduleText=Press 1 to confirm tomorrow...
//         return twilioStudioService.createStudioExecution(toPhone, rescheduleText);
//     }
// }
