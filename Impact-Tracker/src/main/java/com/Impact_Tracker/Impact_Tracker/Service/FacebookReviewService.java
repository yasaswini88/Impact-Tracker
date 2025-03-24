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

    private static final String API_TOKEN = "apify_api_UInSDeXoqdWqdj7ATc3hggHx5pqjJ32zeBvR";
    private static final String APIFY_FB_ENDPOINT =
            "https://api.apify.com/v2/acts/apify~facebook-reviews-scraper/runs?token=" + API_TOKEN;

    // @Scheduled(cron = "0 0/2 * * * ?")  // Runs every 2 minutes
    public void scheduledFacebookReviewFetch() throws InterruptedException {
        System.out.println(">>> Starting scheduledFacebookReviewFetch for businessId=4");

        Long targetBusinessId = 1L;
        Business business = businessRepository.findById(targetBusinessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID=" + targetBusinessId));

        System.out.println("Processing hardcoded business ID=" + targetBusinessId);

        String facebookReviewUrl = "https://www.facebook.com/ProfessionalLawnCareLLC/reviews";
        if (facebookReviewUrl == null) {
            System.out.println("No Facebook URL for business ID=" + business.getBusinessId());
            return;
        }

        Map<String, Object> requestBody = Map.of(
                "startUrls", List.of(Map.of("url", facebookReviewUrl)),
                "resultsLimit", 20
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        System.out.println("Calling Apify API to trigger scraper run...");
        Map<String, Object> runResponse = restTemplate.postForObject(
                APIFY_FB_ENDPOINT,
                httpEntity,
                Map.class
        );

        if (runResponse == null || runResponse.get("data") == null) {
            System.out.println("No Apify run response for business ID=" + business.getBusinessId());
            return;
        }

        String datasetId = (String) ((Map<String, Object>) runResponse.get("data")).get("defaultDatasetId");
        if (datasetId == null) {
            System.out.println("Apify returned null datasetId for business ID=" + business.getBusinessId());
            return;
        }

        System.out.println("Apify run started. Dataset ID: " + datasetId);
        System.out.println("Waiting for Apify dataset to be generated...");
        Thread.sleep(5000);  // Give Apify time to scrape reviews

        // String datasetItemsUrl = "https://api.apify.com/v2/datasets/" + datasetId + "/items?clean=true&format=json&limit=1000&view=overview";
        String datasetItemsUrl = "https://api.apify.com/v2/datasets/4c2ah7hNBTVsvSVqg/items?clean=true&format=json&limit=1000&view=overview";
        System.out.println("Fetching dataset from: " + datasetItemsUrl);
        List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetItemsUrl, List.class);

        if (reviewsList == null || reviewsList.isEmpty()) {
            System.out.println("No Facebook reviews found in Apify dataset for business ID=" + business.getBusinessId());
            return;
        }

        System.out.println("Fetched " + reviewsList.size() + " Facebook reviews. Processing...");

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
            System.out.println("Saved review: " + reviewText);
        }

        System.out.println("Saved all Facebook reviews for business ID=" + business.getBusinessId());
        System.out.println("<<< Finished scheduledFacebookReviewFetch.");
    }

    public List<FacebookReview> getReviewsByBusinessId(Long businessId) {
        System.out.println("Fetching reviews for business ID=" + businessId);
        List<FacebookReview> reviews = facebookReviewRepository.findAll()
                .stream()
                .filter(r -> r.getBusiness().getBusinessId().equals(businessId))
                .toList();
        System.out.println("Found " + reviews.size() + " reviews for business ID=" + businessId);
        return reviews;
    }

    public void fetchAndStoreFacebookReviews(Long businessId, String datasetUrl) {
        System.out.println("Manually fetching Facebook reviews for business ID=" + businessId);

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with ID=" + businessId));

        List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetUrl, List.class);

        if (reviewsList == null || reviewsList.isEmpty()) {
            System.out.println("No Facebook reviews found in provided dataset URL for business ID=" + businessId);
            return;
        }

        System.out.println("Fetched " + reviewsList.size() + " reviews from dataset URL. Saving...");

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
            System.out.println("Saved review from dataset URL: " + reviewText);
        }

        System.out.println("Stored all Facebook reviews for business ID=" + businessId);
    }
}
