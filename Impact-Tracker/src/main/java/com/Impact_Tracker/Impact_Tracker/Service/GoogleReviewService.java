package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.GoogleReview;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.GoogleReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class GoogleReviewService {

    @Autowired
    private GoogleReviewRepository googleReviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String API_TOKEN = "apify_api_UInSDeXoqdWqdj7ATc3hggHx5pqjJ32zeBvR"; 
    // The actor endpoint:
    private static final String APIFY_ENDPOINT = 
       "https://api.apify.com/v2/acts/compass~google-maps-reviews-scraper/runs?token=" + API_TOKEN;

    /**
     * Scheduled method to fetch reviews from Apify for a particular business.
     * Example: run once daily at 2 AM server time
     */
    @Scheduled(cron = "0 0 5 * * *")  
    //   @Scheduled(cron = "0 0/2 * * * ?")
    public void fetchAndStoreReviews() throws InterruptedException {
    
        Long businessId = 3L;
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

       
        String googlePlaceUrl = business.getGooglePlacesLink(); 
        if (googlePlaceUrl == null) {
            System.out.println("No googlePlacesLink set for business ID=" + businessId);
            return;
        }

        // Build the request body 
     Map<String, Object> requestBody = Map.of(
            "startUrls", List.of(
                Map.of("url", googlePlaceUrl)
            ),
            "maxReviews", 25,
            "language", "en"
        );

              HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2) Wrap the requestBody + headers in HttpEntity
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        // 3) Now do postForObject with the httpEntity instead of the raw Map
        Map<String, Object> runResponse = restTemplate.postForObject(
            APIFY_ENDPOINT, 
            httpEntity, 
            Map.class
        );

      
      

        if (runResponse == null) {
            System.out.println("No response from Apify run");
            return;
        }

   

        String datasetId = (String) ((Map<String, Object>) runResponse.get("data")).get("defaultDatasetId");
        try{
        if (datasetId == null) {
            System.out.println("No datasetId from the run. Possibly an error.");
            Thread.sleep(5000); 
            return;
        }
        }catch(InterruptedException e){
            System.out.println("Error in thread sleep");
        }

        System.out.println("Got datasetId: " + datasetId);
        Thread.sleep(5000); // Wait a bit for Apify to process the data
        System.out.println("pulling details datasetId: " + datasetId);

        // Wait a bit for Apify to process the data

        String datasetItemsUrl = "https://api.apify.com/v2/datasets/" 
                         + datasetId 
                         + "/items?"
                         + "clean=true&format=json&view=overview";

        System.out.println("Fetching reviews from: " + datasetItemsUrl);

        datasetItemsUrl = "https://api.apify.com/v2/datasets/gzLpLNOmAHBEzkeJ9/items?clean=true&format=json&view=overview";
List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetItemsUrl, List.class);
if (reviewsList == null || reviewsList.isEmpty()) {
    System.out.println("No reviews found");
    return;
}

for (Map<String, Object> reviewObj : reviewsList) {

    String reviewText = (String) reviewObj.get("text");
    Number starNumber = (Number) reviewObj.get("stars"); 
    Integer stars = (starNumber != null ? starNumber.intValue() : null);

    // Grab the actual published date from the JSON
    // String publishedAtDateStr = (String) reviewObj.get("publishedAtDate");

    // LocalDateTime publishedAt = null;
    // if (publishedAtDateStr != null && !publishedAtDateStr.isEmpty()) {
    //     // Parse the "2025-02-05T23:59:59.999Z" into LocalDateTime
    //     OffsetDateTime odt = OffsetDateTime.parse(publishedAtDateStr, DateTimeFormatter.ISO_DATE_TIME);
    //     publishedAt = odt.toLocalDateTime();
    // }

    if (reviewText != null && !reviewText.isEmpty()) {
        GoogleReview googleReview = new GoogleReview();
        googleReview.setReviewText(reviewText);
        googleReview.setStars(stars);
        googleReview.setBusiness(business);
        // googleReview.setPublishedAt(publishedAt);  

        googleReviewRepository.save(googleReview);
    }
}
System.out.println("Saved " + reviewsList.size() + " reviews for business ID=" + businessId);
    }


    public void fetchReviewsForBusiness(Long businessId) {
       
    }
    // A method to get all reviews from DB or by businessId
    public List<GoogleReview> getReviewsByBusinessId(Long businessId) {
        // e.g. could do a custom query or filter after the fact:
        return googleReviewRepository.findAll()
            .stream()
            .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
            .toList();
    }

        public List<String> getReviewsTextByBusinessId(Long businessId) {
        return googleReviewRepository.findAll()
            .stream()
            .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
            .map(GoogleReview::getReviewText)  // Just the text, not the entire entity
            .toList();
    }
}




































     // 4) Apify usually responds with a "data" or "defaultDatasetId". 
        //    Then you retrieve the dataset items. 
        //    E.g. you might do a second GET to 
        //    https://api.apify.com/v2/datasets/<datasetId>/items?token=API_TOKEN
        //    Let’s assume we can fetch the items directly from the runResponse, or if 
        //    you have "datasetId" you do a second call. That is the typical pattern: