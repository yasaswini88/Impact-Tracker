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
        return "{\"trend_summary\": \"No data for this period.\", \"overall_sentiment\": \"Neutral\"}";
    }

    String prompt = "Analyze these phone call reviews with their creation dates. " +
            "Summarize how sentiment has changed or progressed over the past week in 1-2 sentences. " +
            "Also, provide an overall sentiment as a single word: Positive, Negative, or Neutral, based on the reviews. " +
            "Return your response strictly in valid JSON format:\n" +
            "{\n" +
            "  \"trend_summary\": \"...\",\n" +
            "  \"overall_sentiment\": \"Positive\"|\"Negative\"|\"Neutral\"\n" +
            "}\n" +
            "Here is the data:\n" +
            String.join("\n", lines);

    Map<String, Object> requestBody = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(
                    Map.of("role", "system", "content",
                            "You are an AI that analyzes customer reviews and extracts short trend summaries along with overall sentiment."),
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
        return "{\"trend_summary\": \"OpenAI request failed.\", \"overall_sentiment\": \"Neutral\"}";
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

    public String generateSeasonalTrends(String businessType, String googlePlacesLink)
     {
        String prompt = "Provide estimated seasonal demand month-wise for a " + businessType
            + " business located at " + googlePlacesLink + ". "
            + "The demand should be on a percentage scale where 0% is no demand and 100% is the peak demand month. "
            + "Ensure that one or more months (typically summer) have values close to 100%, "
            + "winter months have lower percentages, and show a smooth seasonal variation. "
            + "Respond strictly in JSON format like this:\n"
            + "{\"January\":percentage,\"February\":percentage,...,\"December\":percentage} "
            + "Only output the JSON, with no extra text.";

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

    public String generateGlobalSeasonalTrends(String businessType, String location) {
    String prompt = String.format(
        "Provide estimated global seasonal demand (month-wise) for %s businesses. " +
        "Demand should be represented on a percentage scale from 0%% (lowest demand) to 100%% (highest demand month). " +
        "The response must strictly be in JSON format:\n" +
        "{\"January\":percentage,\"February\":percentage,\"March\":percentage,\"April\":percentage,\"May\":percentage,\"June\":percentage,\"July\":percentage,\"August\":percentage,\"September\":percentage,\"October\":percentage,\"November\":percentage,\"December\":percentage}. " +
        "Return only the JSON without any additional text.",
        businessType
    );

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

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
        OPENAI_URL,
        HttpMethod.POST,
        requestEntity,
        Map.class
    );

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        return extractInsights(response.getBody());
    } else {
        throw new RuntimeException("OpenAI API failed: " + response.getStatusCodeValue());
    }



}

public String suggestFeatures(String businessType, String googleInsights, String facebookInsights, String callVolumeSummary, List<String> featureList) {
     String prompt = String.format(
        "You are an expert consultant specializing in feature recommendations for businesses.\n\n" +
        "Given the following data about a '%s' business:\n\n" +
        "Google Reviews Insights:\n%s\n\n" +
        "Facebook Reviews Insights:\n%s\n\n" +
        "Call Volume Analysis:\n%s\n\n" +
        "Available Features:\n%s\n\n" +
        "Based on the customer sentiment (positives and negatives), call volume trends, and the overall business context provided, identify and recommend ALL suitable features from the provided feature list that would significantly benefit this business. \n\n" +
        "Prioritize features that directly address customer concerns mentioned in reviews, resolve pain points related to declining call volumes, or capitalize on strengths. Aim to recommend at least 3 to 5 relevant features if possible. Provide your answer strictly as a JSON array of feature names without any additional text:\n\n" +
        "[\"Feature Name 1\", \"Feature Name 2\", \"Feature Name 3\", ...]",
        businessType, googleInsights, facebookInsights, callVolumeSummary, featureList
    );


    System.out.println("---------------------------------------------------------");

    System.out.println("Business Type: " + businessType);   
    System.out.println("Prompt: " + prompt);
    // System.out.println(prompt);
    System.out.println("Feature List: " + featureList);
    System.out.println("Google Insights: " + googleInsights);
    System.out.println("Facebook Insights: " + facebookInsights);
    System.out.println("Call Volume Summary: " + callVolumeSummary);



    System.out.println("---------------------------------------------------------");

    Map<String, Object> requestBody = Map.of(
        "model", "gpt-3.5-turbo",
        "messages", List.of(
            Map.of("role", "system", "content", "Suggest relevant features based on insights provided."),
            Map.of("role", "user", "content", prompt)
        ),
        "temperature", 0.5
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    ResponseEntity<Map> response = restTemplate.exchange(
        OPENAI_URL,
        HttpMethod.POST,
        new HttpEntity<>(requestBody, headers),
        Map.class
    );

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        return extractInsights(response.getBody());
    } else {
        throw new RuntimeException("OpenAI API failed: " + response.getStatusCode());
    }
}


}
