package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.Business;
import com.Impact_Tracker.Impact_Tracker.Entity.FacebookReview;
import com.Impact_Tracker.Impact_Tracker.Repo.BusinessRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.FacebookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class FacebookReviewService {

    @Autowired
    private FacebookReviewRepository facebookReviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String API_TOKEN = "apify_api_UInSDeXoqdWqdj7ATc3hggHx5pqjJ32zeBvR"; // Replace with your token
   private static final String APIFY_FB_ENDPOINT =
        "https://api.apify.com/v2/acts/apify~facebook-reviews-scraper/runs?token=" + API_TOKEN;


    // @Scheduled(cron = "0 0 4 * * *")  // Example: run every day at 4 AM
    @Scheduled(cron = "0 0/2 * * * ?")  // Example: run every 2 minutes
    public void scheduledFacebookReviewFetch() throws InterruptedException {
        // Fetch businesses where you have Facebook review URLs (store them if not done yet, or hardcode)
        List<Business> businesses = businessRepository.findAll();

        for (Business business : businesses) {
            String facebookReviewUrl = "https://www.facebook.com/Katiecleansva/reviews"; // Example; ideally add `business.getFacebookUrl()` field if needed

            if (facebookReviewUrl == null) {
                System.out.println("No Facebook URL for business ID=" + business.getBusinessId());
                continue;
            }

           Map<String, Object> requestBody = Map.of(
    "startUrls", List.of(
        Map.of("url", facebookReviewUrl)
    ),
    "resultsLimit", 20
);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> runResponse = restTemplate.postForObject(
                    APIFY_FB_ENDPOINT,
                    httpEntity,
                    Map.class
            );

            if (runResponse == null || runResponse.get("data") == null) {
                System.out.println("No Apify run response for business ID=" + business.getBusinessId());
                continue;
            }

            String datasetId = (String) ((Map<String, Object>) runResponse.get("data")).get("defaultDatasetId");
            if (datasetId == null) {
                System.out.println("Apify returned null datasetId");
                continue;
            }

            System.out.println("Waiting for dataset generation (Facebook)...");
            Thread.sleep(5000);  // Give Apify time to process

            String datasetItemsUrl = "https://api.apify.com/v2/datasets/" + datasetId + "/items?clean=true&format=json&limit=1000&view=overview";
            List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetItemsUrl, List.class);

            if (reviewsList == null || reviewsList.isEmpty()) {
                System.out.println("No Facebook reviews found in dataset");
                continue;
            }

            for (Map<String, Object> review : reviewsList) {
                Map<String, Object> userMap = (Map<String, Object>) review.get("user");
                String reviewText = (String) review.get("text");
                String date = (String) review.get("date");
                Integer likesCount = (Integer) review.get("likesCount");
                Integer commentsCount = (Integer) review.get("commentsCount");

                String reviewerName = userMap != null ? (String) userMap.get("name") : null;
                String reviewerPic = userMap != null ? (String) userMap.get("profilePic") : null;

                OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

                FacebookReview fbReview = new FacebookReview();
                fbReview.setReviewText(reviewText);
                fbReview.setReviewDate(offsetDateTime.toLocalDateTime());
                fbReview.setLikesCount(likesCount);
                fbReview.setCommentsCount(commentsCount);
                fbReview.setReviewerName(reviewerName);
                fbReview.setReviewerProfilePicUrl(reviewerPic);
                fbReview.setBusiness(business);

                facebookReviewRepository.save(fbReview);
            }

            System.out.println("Saved " + reviewsList.size() + " Facebook reviews for business " + business.getBusinessId());
        }
    }

    public List<FacebookReview> getReviewsByBusinessId(Long businessId) {
        return facebookReviewRepository.findAll()
                .stream()
                .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
                .toList();
    }

    public void fetchAndStoreFacebookReviews(Long businessId, String datasetUrl) {
    Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new RuntimeException("Business not found"));

    List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetUrl, List.class);

    if (reviewsList == null || reviewsList.isEmpty()) {
        System.out.println("No Facebook reviews found in dataset URL");
        return;
    }

    for (Map<String, Object> review : reviewsList) {
        Map<String, Object> userMap = (Map<String, Object>) review.get("user");
        String reviewText = (String) review.get("text");
        String date = (String) review.get("date");
        Integer likesCount = (Integer) review.get("likesCount");
        Integer commentsCount = (Integer) review.get("commentsCount");

        String reviewerName = userMap != null ? (String) userMap.get("name") : null;
        String reviewerPic = userMap != null ? (String) userMap.get("profilePic") : null;

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

        FacebookReview fbReview = new FacebookReview();
        fbReview.setReviewText(reviewText);
        fbReview.setReviewDate(offsetDateTime.toLocalDateTime());
        fbReview.setLikesCount(likesCount);
        fbReview.setCommentsCount(commentsCount);
        fbReview.setReviewerName(reviewerName);
        fbReview.setReviewerProfilePicUrl(reviewerPic);
        fbReview.setBusiness(business);

        facebookReviewRepository.save(fbReview);
    }

    System.out.println("Stored " + reviewsList.size() + " Facebook reviews for business " + businessId);
}

}
