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

    public String analyzeReviews(List<String> reviews) {
        if (reviews.isEmpty()) {
            return "No reviews found for this business.";
        }

        String prompt = "Analyze these customer reviews and provide insights strictly in JSON format.\n" +
                "Your response should contain only a valid JSON object, nothing else. No additional text.\n" +
                "The JSON structure must be:\n" +
                "{\n" +
                "  \"insights\": {\n" +
                "    \"overall_summary\": \"...\",\n" +
                "    \"positive\": \"...\",\n" +
                "    \"negative\": \"...\"\n" +
                "  }\n" +
                "}\n" +
                "Reviews:\n" +
                String.join("\n", reviews);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an AI that analyzes customer reviews and extracts insights."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

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

        String prompt = "Analyze these phone call reviews with their creation dates. Summarize how sentiment has changed " +
                "or progressed over the past week in 1-2 sentences. " +
                "Return your response in valid JSON with a single field: 'trend_summary'. " +
                "Here is the data:\n" +
                String.join("\n", lines);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "You are an AI that analyzes customer reviews and extracts short trend summaries."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response =
                restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return extractInsights(response.getBody());
        } else {
            return "{\"trend_summary\": \"OpenAI request failed with status: " +
                    response.getStatusCodeValue() + "\"}";
        }
    }

    private String extractInsights(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message != null && message.containsKey("content")) {
                String content = message.get("content").toString();
                content = content.replaceAll("```[a-zA-Z]*", "").replaceAll("```", "");
                return content.trim();
            }
        }
        return "No insights found in response.";
    }

    public String analyzeCallVolumeData(
            List<Integer> answered,
            List<Integer> missed,
            List<Integer> voicemail,
            List<String> months,
            String businessType,
            String address
    ) {
        String prompt = "Assuming call volume is proportional to sales, Analyze the monthly call volume data for a "
                + (businessType != null ? businessType : "business")
                + " located at " + (address != null ? address : "Unknown Address") + ". "
                + "We have call counts for last 7 months. "
                + "Please return a SHORT summary of down trend in the sales for  business  "
                + "and also mention whether these trends are affecting this business. "
                + "Respond ONLY in valid JSON with one field 'call_volume_summary'.\n\n";

        for (int i = 0; i < months.size(); i++) {
            prompt += String.format("Month: %s, Answered: %d \n",
                    months.get(i), answered.get(i));
        }

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an AI that summarizes call volume data."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response =
                restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return extractInsights(response.getBody());
        } else {
            return "{\"call_volume_summary\": \"OpenAI request failed.\"}";
        }
    }

    public String generateSeasonalTrends(String businessType, String googlePlacesLink) {
        String prompt = "Provide estimated seasonal demand month-wise for a " + businessType
                + " business located at " + googlePlacesLink + ". Respond strictly in JSON:\n"
                + "{\"January\":value,\"February\":value,...,\"December\":value}";

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You generate month-wise seasonal demand trends."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.5
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ResponseEntity<Map> response = restTemplate.exchange(
                OPENAI_URL, HttpMethod.POST, new HttpEntity<>(requestBody, headers), Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return extractInsights(response.getBody());
        } else {
            throw new RuntimeException("OpenAI API failed: " + response.getStatusCode());
        }
    }
}
