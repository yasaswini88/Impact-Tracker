package com.Impact_Tracker.Impact_Tracker.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {



    @Value("${openai.apiKey}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    


    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * Analyzes the given list of reviews, returning a summarized "insights" string.
     */
    public String analyzeReviews(List<String> reviews) {
        if (reviews.isEmpty()) {
            return "No reviews found for this business.";
        }

        // 1) Combine the reviews into a single prompt
       String prompt = "Analyze these customer reviews and provide insights:\n" +
                    "Your entire output must be valid JSON with a single top-level key: \"insights\".\n" +
                    "Please:\n" +
                    "1) Summarize the primary positive and negative points in 1-2 sentences max.\n" +
                    "2) Return the result in this exact JSON format, for example:\n" +
                    "{\"insights\": \"Short summary goes here.\"}\n" +
                    String.join("\n", reviews);


        // 2) Build the request body. For example, using GPT-3.5 or GPT-4
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-3.5-turbo",  // or "gpt-4" if you have access
            "messages", List.of(
                Map.of("role", "system", "content", "You are an AI that analyzes customer reviews and extracts insights."),
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.7
            // You can add "max_tokens" or other parameters if you like
        );

        // 3) Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // Bearer token from your .properties

        // 4) Create request entity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 5) Make the POST request
        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

        // 6) Parse the response
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return extractInsights(response.getBody());
        } else {
            return "OpenAI request failed with status: " + response.getStatusCodeValue();
        }
    }


    public String analyzeWeeklyTrendData(List<String> lines) {
        if (lines.isEmpty()) {
            return "{\"trend_summary\": \"No data for this period.\"}";
        }

       
        String prompt =
                "Analyze these phone call reviews with their creation dates. Summarize how sentiment has changed " +
                "or progressed over the past week in 1-2 sentences. " +
                "Return your response in valid JSON with a single field: 'trend_summary'. " +
                "Here is the data:\n" + 
                String.join("\n", lines);

        // 2) Build the request body
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content",
                               "You are an AI that analyzes customer reviews and extracts short trend summaries."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
                // You can add max_tokens or other parameters if desired
        );

        // 3) Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 4) Send request
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response =
                restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

        // 5) Parse the response
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return extractInsights(response.getBody());
        } else {
            return "{\"trend_summary\": \"OpenAI request failed with status: " +
                    response.getStatusCodeValue() + "\"}";
        }
    }


    /**
     * Helper method to extract the assistant's content from the response JSON.
     */
    private String extractInsights(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices != null && !choices.isEmpty()) {
            // typically first choice
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message != null && message.containsKey("content")) {
                return message.get("content").toString();
            }
        }
        return "No insights found in response.";
    }
}
