package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunication;
import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunicationReview;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationRepository;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MapCommunicationReviewService {

    @Autowired
    private MapCommunicationReviewRepository reviewRepository;

    @Autowired
    private MapCommunicationRepository mapCommunicationRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String API_TOKEN = "apify_api_UInSDeXoqdWqdj7ATc3hggHx5pqjJ32zeBvR";
    private static final String APIFY_ENDPOINT = "https://api.apify.com/v2/acts/compass~google-maps-reviews-scraper/runs?token=" + API_TOKEN;

    // Scheduled method cannot accept parameters
    // @Scheduled(cron = "0 0/3 * * * ?")
    public void fetchAndStoreMapCommunicationReviews() throws InterruptedException {
        // Fetch all MapCommunication records or a specific subset
        List<MapCommunication> communications = mapCommunicationRepository.findAll();

        for (MapCommunication mapComm : communications) {
            Long mapCommunicationId = mapComm.getId();
            String googlePlaceUrl = mapComm.getGooglePlacesLink();

            if (googlePlaceUrl == null || googlePlaceUrl.isEmpty()) {
                System.out.println("No googlePlacesLink set for Map Communication ID=" + mapCommunicationId);
                continue;
            }

            Map<String, Object> requestBody = Map.of(
                "startUrls", List.of(Map.of("url", googlePlaceUrl)),
                "maxReviews", 25,
                "language", "en"
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> runResponse = restTemplate.postForObject(APIFY_ENDPOINT, httpEntity, Map.class);
            if (runResponse == null) {
                System.out.println("No response from Apify run");
                continue;
            }

            // You may dynamically fetch dataset URL from the runResponse if provided
            String datasetItemsUrl = "https://api.apify.com/v2/datasets/e7Szp25dXti1gWM4G/items?clean=true&format=json&limit=1000&view=overview";
            List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetItemsUrl, List.class);
            if (reviewsList == null || reviewsList.isEmpty()) {
                System.out.println("No reviews found for Map Communication ID=" + mapCommunicationId);
                continue;
            }

            for (Map<String, Object> reviewObj : reviewsList) {
                String reviewText = (String) reviewObj.get("text");
                Number starNumber = (Number) reviewObj.get("stars");
                Integer stars = (starNumber != null ? starNumber.intValue() : null);

                if (reviewText != null && !reviewText.isEmpty()) {
                    MapCommunicationReview review = new MapCommunicationReview();
                    review.setReviewText(reviewText);
                    review.setStars(stars);
                    review.setMapCommunication(mapComm);
                    review.setPublishedAt(LocalDateTime.now());

                    reviewRepository.save(review);
                }
            }
            System.out.println("Saved " + reviewsList.size() + " reviews for Map Communication ID=" + mapCommunicationId);
        }
    }


    public void fetchAndStoreMapCommunicationReviews(Long mapCommunicationId) throws InterruptedException {
        MapCommunication mapComm = mapCommunicationRepository.findById(mapCommunicationId)
            .orElseThrow(() -> new RuntimeException("Map Communication not found with ID: " + mapCommunicationId));
    
        String googlePlaceUrl = mapComm.getGooglePlacesLink();
        if (googlePlaceUrl == null || googlePlaceUrl.isEmpty()) {
            System.out.println("No googlePlacesLink set for Map Communication ID=" + mapCommunicationId);
            return;
        }
    
        Map<String, Object> requestBody = Map.of(
            "startUrls", List.of(Map.of("url", googlePlaceUrl)),
            "maxReviews", 25,
            "language", "en"
        );
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
    
        Map<String, Object> runResponse = restTemplate.postForObject(APIFY_ENDPOINT, httpEntity, Map.class);
        if (runResponse == null) {
            System.out.println("No response from Apify run");
            return;
        }
    
        String datasetItemsUrl = "https://api.apify.com/v2/datasets/Jz0ygUyAfedphQCcH/items?clean=true&format=json&limit=1000&view=overview";
        List<Map<String, Object>> reviewsList = restTemplate.getForObject(datasetItemsUrl, List.class);
        if (reviewsList == null || reviewsList.isEmpty()) {
            System.out.println("No reviews found");
            return;
        }
    
        for (Map<String, Object> reviewObj : reviewsList) {
            String reviewText = (String) reviewObj.get("text");
            Number starNumber = (Number) reviewObj.get("stars");
            Integer stars = (starNumber != null ? starNumber.intValue() : null);
    
            if (reviewText != null && !reviewText.isEmpty()) {
                MapCommunicationReview review = new MapCommunicationReview();
                review.setReviewText(reviewText);
                review.setStars(stars);
                review.setMapCommunication(mapComm);
                review.setPublishedAt(LocalDateTime.now());
                reviewRepository.save(review);
            }
        }
    
        System.out.println("Saved " + reviewsList.size() + " reviews for Map Communication ID=" + mapCommunicationId);
    }

    public List<MapCommunicationReview> getReviewsByMapCommunicationId(Long mapCommunicationId) {
        return reviewRepository.findAll().stream()
            .filter(r -> r.getMapCommunication().getId().equals(mapCommunicationId))
            .toList();
    }
}

